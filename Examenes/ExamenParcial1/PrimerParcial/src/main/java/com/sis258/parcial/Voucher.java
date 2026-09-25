/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sis258.parcial;

/**
 *
 * @author PC
 */
public class Voucher {
    boolean confirmado;
    String codigoCompra;
    String motivo;
    double montoUSD;
    public Voucher (boolean confirmado, String codigoCompra, String motivo, double montoUSD){
        this.confirmado = confirmado;
        this.codigoCompra = codigoCompra;
        this.motivo = motivo;
        this.montoUSD = montoUSD;
    }
    
   public boolean getConfirmado(){
       return confirmado;
   }
   
   public void setconfirmado(boolean confirmado){
       this.confirmado = confirmado;
   }
   
   public String getCodigoCompra(){
       return codigoCompra;
   }
   
   public void setCodigoCompra(String codigoCompra){
       this.codigoCompra = codigoCompra;
   }
   
   public String getMotivo(){
       return motivo;
   }
   
   public void setMotivo(String motivo){
       this.motivo = motivo;
   }
   
   public double MontoUSD(){
       return montoUSD;
   }
   
   public void setMontoUSD(double montoUSD){
       this.montoUSD = montoUSD;
   }
}
