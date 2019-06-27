package com.estadias.pachuca.interfaces;

import com.estadias.pachuca.fragments.FragmentConsultarNegocio;
import com.estadias.pachuca.fragments.FragmentListaCategorias;
import com.estadias.pachuca.fragments.FragmentListaNegocios;

public interface IFragments extends FragmentListaCategorias.OnFragmentInteractionListener
        , FragmentListaNegocios.OnFragmentInteractionListener
        , FragmentConsultarNegocio.OnFragmentInteractionListener {
}
