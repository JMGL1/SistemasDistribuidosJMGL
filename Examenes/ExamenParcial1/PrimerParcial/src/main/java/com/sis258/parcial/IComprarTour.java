/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sis258.parcial;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author PC
 */

public interface IComprarTour extends Remote{
    Voucher comprar(int pasaporte, String codigoTour, int personas) throws RemoteException;
}
