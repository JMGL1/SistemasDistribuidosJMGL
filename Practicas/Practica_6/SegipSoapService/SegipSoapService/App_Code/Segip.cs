using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;

/// <summary>
/// Descripción breve de Segip
/// </summary>
[WebService(Namespace = "http://tempuri.org/")]
[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
// Para permitir que se llame a este servicio web desde un script, usando ASP.NET AJAX, quite la marca de comentario de la línea siguiente. 
// [System.Web.Script.Services.ScriptService]
public class Segip : WebService
{
    [WebMethod]
    public bool verificarCI(string ci)
    {
        if (string.IsNullOrEmpty(ci) || ci.Length < 5)
        {
            return false;
        }
        return true;
    }
}