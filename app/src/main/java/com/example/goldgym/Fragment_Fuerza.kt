package com.example.goldgym

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class Fragment_Fuerza : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__fuerza, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() :Fragment_Fuerza{
            return  Fragment_Fuerza()
        }

    }
}