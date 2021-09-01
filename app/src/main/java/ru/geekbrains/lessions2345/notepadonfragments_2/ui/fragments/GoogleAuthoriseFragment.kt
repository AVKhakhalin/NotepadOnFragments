package ru.geekbrains.lessions2345.notepadonfragments_2.ui.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import ru.geekbrains.lessions2345.notepadonfragments_2.R
import ru.geekbrains.lessions2345.notepadonfragments_2.observe.Publisher
import ru.geekbrains.lessions2345.notepadonfragments_2.observe.PublisherGetter
import ru.geekbrains.lessions2345.notepadonfragments_2.ui.MainActivity
import ru.geekbrains.lessions2345.notepadonfragments_2.ui.Navigation

class GoogleAuthoriseFragment : DialogFragment(), DialogInterface.OnClickListener {
    // Используется, чтобы определить результат Activity регистрации через Google
    private val RC_SIGN_IN : Int = 40404

    private var navigation : Navigation? = null
    private var publisher = Publisher()

    // Клиент для регистрации пользователя через Google
    private var googleSignInClient : GoogleSignInClient? = null

    // Кнопка регистрации через Google
    private var buttonSignIn : SignInButton? = null

    // Кнопка выхода из Google
    private var buttonSingOut : MaterialButton? = null
    private var emailView : TextView? = null
    private var button_continue : MaterialButton? = null

    fun newInstance() : GoogleAuthoriseFragmentOriginalJava {
        return GoogleAuthoriseFragmentOriginalJava()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Получим навигацию по приложению, чтобы перейти на фрагмент со списком карточек
        val activity = context as MainActivity
        navigation = activity.navigation
        // Получаем паблишер, чтобы передать с ним в MainActivity результат аутентификации через Google
        publisher = (context as PublisherGetter).publisher
    }

    override fun onDetach() {
        navigation = null
        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_google_authorise, container, false)
        initGoogleSign()
        initView(view)
        enableSign()
        signOut()
        return view
    }

    // Инициализация запроса на аутентификацию
    private fun initGoogleSign() {
        // Конфигурация запроса на регистрацию пользователя, чтобы получить
        // идентификатор пользователя, его почту и основной профайл
        // (регулируется параметром)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Получаем клиента для регистрации и данные по клиенту
        val activity = context as MainActivity
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    // Инициализация пользовательских элементов
    private fun initView(view: View) {
        // Кнопка регистрации пользователя
        buttonSignIn = view.findViewById(R.id.sign_in_button)
        if (buttonSignIn != null) {
            buttonSignIn!!.setOnClickListener(View.OnClickListener { signIn() }
            )
        }
        emailView = view.findViewById(R.id.email)

        // Кнопка продолжить, будем показывать главный фрагмент
        button_continue = view.findViewById(R.id.continue_button)
        if (button_continue != null) {
            button_continue!!.setOnClickListener(View.OnClickListener {
                Toast.makeText(getContext(), "Авторизация успешно прошла!", Toast.LENGTH_SHORT).show()
                publisher.notifySingle(true)
                dismiss()
            })
        }

        // Кнопка выхода
        buttonSingOut = view.findViewById(R.id.sing_out_button)
        if (buttonSingOut != null) {
            buttonSingOut!!.setOnClickListener(View.OnClickListener { signOut() })
        }
    }

    override fun onStart() {
        super.onStart()
        // Проверим, входил ли пользователь в это приложение через Google
        val account = GoogleSignIn.getLastSignedInAccount(getContext())
        if (account != null) {
            // Пользователь уже входил, сделаем кнопку недоступной
            disableSign()
            // Обновим почтовый адрес этого пользователя и выведем его на экран
            account.email?.let { updateUI(it) }
        }
    }

    // Выход из учётной записи в приложении
    private fun signOut() {
        if (googleSignInClient != null) {
            googleSignInClient!!.signOut()
                .addOnCompleteListener {
                    updateUI("Для запуска приложения \"Заметки\" Вам нужно аутентифицироваться через\nGoogle:")
                    enableSign()
                }
        }
    }

    // Инициируем регистрацию пользователя
    private fun signIn() {
        if (googleSignInClient != null) {
            val signInIntent = googleSignInClient!!.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    // Здесь получим ответ от системы, что пользователь вошел
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            // Когда сюда возвращается Task, результаты аутентификации уже
            // готовы
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    //https://developers.google.com/identity/sign-in/android/backend-auth?authuser=1
    // Получаем данные пользователя
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Регистрация прошла успешно
            disableSign()
            if (account != null) {
                updateUI("""Аутентификация прошла успешно по e-mail: ${account!!.email}""".trimIndent())
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure
            // reason. Please refer to the GoogleSignInStatusCodes class
            // reference for more information.
            Toast.makeText(
                context,
                "signInResult:failed code = " + e.statusCode,
                Toast.LENGTH_SHORT
            ).show()
            if (emailView != null) {
                emailView!!.text = "Ошибка при аутентификации: " + e.statusCode
            }
        }
    }

    // Обновляем данные о пользователе на экране
    private fun updateUI(email : String) {
        if (emailView != null) {
            emailView!!.text = email
        }
    }

    // Разрешить аутентификацию и запретить остальные действия
    private fun enableSign() {
        if ((buttonSignIn != null) && (button_continue != null) && (buttonSingOut != null)) {
            buttonSignIn!!.isEnabled = true
            button_continue!!.isEnabled = false
            buttonSingOut!!.isEnabled = false
        }
    }

    // Запретить аутентификацию (уже прошла) и разрешить остальные действия
    private fun disableSign() {
        if ((buttonSignIn != null) && (button_continue != null) && (buttonSingOut != null)) {
            buttonSignIn!!.isEnabled = false
            button_continue!!.isEnabled = true
            buttonSingOut!!.isEnabled = true
        }
    }

    // Стандартный метод мы не используем, так как отключили возможность случайного закрытия данного диалогового фрагмента
    override fun onClick(dialog : DialogInterface, which : Int) {}
}