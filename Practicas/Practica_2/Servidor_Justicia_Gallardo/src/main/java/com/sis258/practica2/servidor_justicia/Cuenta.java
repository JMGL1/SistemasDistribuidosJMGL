/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sis258.practicas.practica2;

/**
 *
 * @author X13
 */
import java.io.Serializable;

// Implementamos Serializable para que el objeto pueda viajar por la red RMI
public class Cuenta implements Serializable {
    private String banco;
    private String numeroCuenta;
    private double saldo;

    public Cuenta(String banco, String numeroCuenta, double saldo) {
        this.banco = banco;
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
    }

    public String getBanco() { return banco; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    @Override
    public String toString() {
        return "Banco: " + banco + " | Nro Cuenta: " + numeroCuenta + " | Saldo: " + saldo;
    }
}
