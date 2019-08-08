package com.estadias.pachuca.interfaces;

import com.estadias.pachuca.fragments.FragmentCanjearCodigo;
import com.estadias.pachuca.fragments.FragmentConsultarInfoCliente;
import com.estadias.pachuca.fragments.FragmentConsultarInfoNegocio;
import com.estadias.pachuca.fragments.FragmentConsultarNegocio;
import com.estadias.pachuca.fragments.FragmentConsultarPromociones;
import com.estadias.pachuca.fragments.FragmentCrearPromociones;
import com.estadias.pachuca.fragments.FragmentListaCategorias;
import com.estadias.pachuca.fragments.FragmentListaNegocios;
import com.estadias.pachuca.fragments.FragmentVerCodigos;
import com.estadias.pachuca.fragments.FragmentVerPromociones;

public interface IFragments extends FragmentListaCategorias.OnFragmentInteractionListener
        , FragmentListaNegocios.OnFragmentInteractionListener
        , FragmentConsultarNegocio.OnFragmentInteractionListener
        , FragmentConsultarPromociones.OnFragmentInteractionListener
        , FragmentCrearPromociones.OnFragmentInteractionListener
        , FragmentVerPromociones.OnFragmentInteractionListener
        , FragmentVerCodigos.OnFragmentInteractionListener
        , FragmentCanjearCodigo.OnFragmentInteractionListener
        , FragmentConsultarInfoNegocio.OnFragmentInteractionListener
        , FragmentConsultarInfoCliente.OnFragmentInteractionListener {
}
