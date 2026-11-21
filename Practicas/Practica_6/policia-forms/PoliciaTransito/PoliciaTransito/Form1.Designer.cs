namespace PoliciaTransito
{
    partial class Form1
    {
        private System.ComponentModel.IContainer components = null;

        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        private void InitializeComponent()
        {
            this.grpMulta = new System.Windows.Forms.GroupBox();
            this.txtTipoInfraccion = new System.Windows.Forms.TextBox();
            this.txtLugar = new System.Windows.Forms.TextBox();
            this.txtMonto = new System.Windows.Forms.TextBox();
            this.btnEmitir = new System.Windows.Forms.Button();
            this.txtCI = new System.Windows.Forms.TextBox();
            this.btnVerificar = new System.Windows.Forms.Button();
            this.lblEstadoVerificacion = new System.Windows.Forms.Label();
            this.grpMulta.SuspendLayout();
            this.SuspendLayout();

            this.grpMulta.Controls.Add(this.btnEmitir);
            this.grpMulta.Controls.Add(this.txtMonto);
            this.grpMulta.Controls.Add(this.txtLugar);
            this.grpMulta.Controls.Add(this.txtTipoInfraccion);
            this.grpMulta.Location = new System.Drawing.Point(20, 100);
            this.grpMulta.Name = "grpMulta";
            this.grpMulta.Size = new System.Drawing.Size(350, 200);
            this.grpMulta.TabIndex = 0;
            this.grpMulta.TabStop = false;
            this.grpMulta.Text = "Datos de Multa";

            this.txtTipoInfraccion.Location = new System.Drawing.Point(20, 40);
            this.txtTipoInfraccion.Name = "txtTipoInfraccion";
            this.txtTipoInfraccion.Size = new System.Drawing.Size(300, 20);
            this.txtTipoInfraccion.TabIndex = 0;

            this.txtLugar.Location = new System.Drawing.Point(20, 80);
            this.txtLugar.Name = "txtLugar";
            this.txtLugar.Size = new System.Drawing.Size(300, 20);
            this.txtLugar.TabIndex = 1;

            this.txtMonto.Location = new System.Drawing.Point(20, 120);
            this.txtMonto.Name = "txtMonto";
            this.txtMonto.Size = new System.Drawing.Size(300, 20);
            this.txtMonto.TabIndex = 2;

            this.btnEmitir.Location = new System.Drawing.Point(20, 160);
            this.btnEmitir.Name = "btnEmitir";
            this.btnEmitir.Size = new System.Drawing.Size(300, 25);
            this.btnEmitir.TabIndex = 3;
            this.btnEmitir.Text = "Emitir Multa";
            this.btnEmitir.UseVisualStyleBackColor = true;
            this.btnEmitir.Click += new System.EventHandler(this.btnEmitir_Click);

            this.txtCI.Location = new System.Drawing.Point(20, 30);
            this.txtCI.Name = "txtCI";
            this.txtCI.Size = new System.Drawing.Size(150, 20);
            this.txtCI.TabIndex = 1;

            this.btnVerificar.Location = new System.Drawing.Point(190, 28);
            this.btnVerificar.Name = "btnVerificar";
            this.btnVerificar.Size = new System.Drawing.Size(180, 25);
            this.btnVerificar.TabIndex = 2;
            this.btnVerificar.Text = "Verificar CI";
            this.btnVerificar.UseVisualStyleBackColor = true;
            this.btnVerificar.Click += new System.EventHandler(this.btnVerificar_Click);

            this.lblEstadoVerificacion.Location = new System.Drawing.Point(20, 65);
            this.lblEstadoVerificacion.Name = "lblEstadoVerificacion";
            this.lblEstadoVerificacion.Size = new System.Drawing.Size(350, 20);
            this.lblEstadoVerificacion.TabIndex = 3;

            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(400, 320);
            this.Controls.Add(this.lblEstadoVerificacion);
            this.Controls.Add(this.btnVerificar);
            this.Controls.Add(this.txtCI);
            this.Controls.Add(this.grpMulta);
            this.Name = "Form1";
            this.Text = "Policia";
            this.grpMulta.ResumeLayout(false);
            this.grpMulta.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();
        }

        private System.Windows.Forms.GroupBox grpMulta;
        private System.Windows.Forms.TextBox txtTipoInfraccion;
        private System.Windows.Forms.TextBox txtLugar;
        private System.Windows.Forms.TextBox txtMonto;
        private System.Windows.Forms.Button btnEmitir;
        private System.Windows.Forms.TextBox txtCI;
        private System.Windows.Forms.Button btnVerificar;
        private System.Windows.Forms.Label lblEstadoVerificacion;
    }
}