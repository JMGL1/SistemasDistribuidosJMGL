/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sis258.parcial;
import java.io.Serializable;
/**
 *
 * @author PC
 */
public class pago implements Serializable{
    boolean aprobado;
    String codigoAutorizacion;
    String motivo;
    
   public pago(boolean aprobado,String codigo,String motivo){
       this.aprobado = aprobado;
       this.codigoAutorizacion = codigoAutorizacion;
       this.motivo = motivo;
   }
   
   public boolean getAprobado(){
       return aprobado;
   }
   
   public void setAprobado(boolean aprobado){
       this.aprobado = aprobado;
   }
   
   public String getCodigoAutorizacion(){
       return codigoAutorizacion;
   }
   
   public void setCodigoAutorizacion(String codigoAutorizacion){
       this.codigoAutorizacion = codigoAutorizacion;
   }
   
   public String getMotivo(){
       return motivo;
   }
   
   public void setMotivo(String motivo){
       this.motivo = motivo;
   }
   
   
    
   
}
