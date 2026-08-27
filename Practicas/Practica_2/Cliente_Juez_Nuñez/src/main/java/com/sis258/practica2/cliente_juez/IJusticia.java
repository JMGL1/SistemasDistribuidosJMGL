package com.sis258.practicas.practica2;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

/**
 *
 * @author X13
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

// Interfaz que define los métodos que el Juez puede invocar remotamente
public interface IJusticia extends Remote {
    RespuestaCuenta consultarCuentas(String ci, String nombres, String apellidos) throws RemoteException;
    boolean congelarMonto(String numeroCuenta, double monto, String banco) throws RemoteException;
}