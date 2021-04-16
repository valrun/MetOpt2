package com.example.metopt.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.metopt.R
import com.example.metopt.SaveFunction
import com.example.metopt.nmethods.QuadraticFunction

class InfoFragment : Fragment() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_info, container, false)

        val a11 = view.findViewById<EditText>(R.id.a11)
        val a12 = view.findViewById<EditText>(R.id.a12)
        val a21 = view.findViewById<EditText>(R.id.a21)
        val a22 = view.findViewById<EditText>(R.id.a22)
        val b1 = view.findViewById<EditText>(R.id.b1)
        val b2 = view.findViewById<EditText>(R.id.b2)
        val c = view.findViewById<EditText>(R.id.c)

        val set = view.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.setFun)
        set.setOnClickListener {
            SaveFunction.function = QuadraticFunction(
                listOf(listOf(a11.text.toString().toDouble(), a12.text.toString().toDouble()),
                    listOf(a21.text.toString().toDouble(), a22.text.toString().toDouble())),
                listOf(b1.text.toString().toDouble(), b2.text.toString().toDouble()),
                c.text.toString().toDouble()
            )
        }
        return view
    }
}


