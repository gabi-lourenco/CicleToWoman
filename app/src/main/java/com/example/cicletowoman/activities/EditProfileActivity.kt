package com.example.cicletowoman.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
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

        btnSave.setOnClickListener {
            try {
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
            } catch (e: Exception) {
                setDataProfileEmpty()
                Toast.makeText(this,"Erro ao salvar os dados!", Toast.LENGTH_LONG).show()
            }
        }
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
                // open edit screen
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
