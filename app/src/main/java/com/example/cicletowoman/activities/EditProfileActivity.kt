package com.example.cicletowoman.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cicletowoman.MyApplication
import com.example.cicletowoman.R
import com.example.cicletowoman.entities.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_first_period.*
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_status_cycle.*
import java.util.*
import kotlin.system.exitProcess

class EditProfileActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    var firebaseDatabase: FirebaseDatabase? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        setSupportActionBar(profiletoolbar)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        val profile = MyApplication.database!!.profileDao().getByUid(auth.currentUser!!.uid)

        try {
            setDataProfile(profile)
        } catch (e: Exception) {
            setDataProfileEmpty()
        }

        btnSave.setOnClickListener { saveData() }
    }

    private fun saveData() {
        try {
            if (fieldsFilled()) {
                MyApplication.database!!.profileDao().insert(
                    Profile(
                        uid = auth.currentUser!!.uid,
                        name = edtName.text.toString(),
                        weight = edtWeigth.text.toString(),
                        height = edtHeight.text.toString(),
                        age = edtAge.text.toString().toInt(),
                    )
                )

                Toast.makeText(this,"Dados salvos com sucesso!", Toast.LENGTH_LONG).show()

                startActivity(
                    Intent(
                        this@EditProfileActivity, StatusCycleActivity::class.java)
                )
            } else {
                Toast.makeText(
                    this,"Preencha todos os dados para salvar!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            setDataProfileEmpty()
            Toast.makeText(this,"Erro ao salvar os dados!", Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteAccount() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle(R.string.first_status_cycle_delete_account_title)
        builder.setMessage(R.string.first_status_cycle_delete_account_message_title)

        builder.setPositiveButton(R.string.first_status_cycle_delete_text_yes) { dialog, which ->
            MyApplication.database!!.cycleDao().delete(auth.currentUser!!.uid)
            MyApplication.database!!.profileDao().delete(auth.currentUser!!.uid)
            Toast.makeText(
                this,
                "Dados apagados com sucesso!",
                Toast.LENGTH_LONG
            ).show()

            auth.signOut()

            moveTaskToBack(true)
            exitProcess(-1)
        }

        builder.setNegativeButton(R.string.first_status_cycle_delete_text_no) { dialog, which -> }

        builder.show()
    }

    private fun fieldsFilled(): Boolean {
        return edtName.text.isNotBlank() &&
            edtHeight.text.isNotBlank() &&
            edtWeigth.text.isNotBlank() &&
            edtAge.text.isNotBlank()
    }

    private fun setDataProfile(profile: Profile) {
        edtName.setText(profile.name.toString())
        edtHeight.setText(profile.height.toString())
        edtWeigth.setText(profile.weight.toString())
        edtAge.setText(profile.age.toString())
    }

    private fun setDataProfileEmpty() {
        edtName.setText(auth.currentUser!!.displayName)
        edtHeight.hint = "Sua altura"
        edtWeigth.hint = "Seu peso"
        edtAge.hint = "Sua idade"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_save -> {
                saveData()
                true
            }
            R.id.action_delete -> {
                deleteAccount()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
