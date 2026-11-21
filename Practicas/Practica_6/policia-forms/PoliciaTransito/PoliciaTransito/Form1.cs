using System;
using System.Net;
using System.Text;
using System.Windows.Forms;

namespace PoliciaTransito
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
            grpMulta.Enabled = false;
        }

        private void btnVerificar_Click(object sender, EventArgs e)
        {
            string ci = txtCI.Text.Trim();
            if (string.IsNullOrEmpty(ci)) return;

            try
            {
                srSegip.SegipSoapClient soapClient = new srSegip.SegipSoapClient();
                bool existe = soapClient.VerificarCI(ci);

                if (existe)
                {
                    lblEstadoVerificacion.Text = "CI válido";
                    grpMulta.Enabled = true;
                }
                else
                {
                    lblEstadoVerificacion.Text = "CI inválido";
                    grpMulta.Enabled = false;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnEmitir_Click(object sender, EventArgs e)
        {
            try
            {
                string ci = txtCI.Text.Trim();
                string codigoUnico = "BOL-" + DateTime.Now.ToString("yyyyMMddHHmmss");
                string tipo = txtTipoInfraccion.Text.Trim();
                string lugar = txtLugar.Text.Trim();
                string monto = txtMonto.Text.Trim();
                string fecha = DateTime.Now.ToString("yyyy-MM-dd");

                string json = $"{{\"codigo_unico\":\"{codigoUnico}\", \"ci_conductor\":\"{ci}\", \"fecha\":\"{fecha}\", \"tipo_infraccion\":\"{tipo}\", \"lugar\":\"{lugar}\", \"monto\":{monto}}}";

                using (WebClient client = new WebClient())
                {
                    client.Headers[HttpRequestHeader.ContentType] = "application/json";
                    client.Encoding = Encoding.UTF8;
                    client.UploadString("http://localhost:8000/api/multas", "POST", json);

                    MessageBox.Show(codigoUnico);
                    grpMulta.Enabled = false;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }
    }
}