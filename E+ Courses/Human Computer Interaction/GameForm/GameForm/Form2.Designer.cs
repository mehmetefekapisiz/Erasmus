namespace GameForm
{
    partial class Form2
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            button1 = new Button();
            label1 = new Label();
            label2 = new Label();
            label3 = new Label();
            label4 = new Label();
            textBox1 = new TextBox();
            button2 = new Button();
            SuspendLayout();
            // 
            // button1
            // 
            button1.BackColor = Color.DarkRed;
            button1.FlatStyle = FlatStyle.Popup;
            button1.ForeColor = SystemColors.Window;
            button1.Location = new Point(1343, 12);
            button1.Name = "button1";
            button1.Size = new Size(45, 45);
            button1.TabIndex = 1;
            button1.Text = "X";
            button1.UseVisualStyleBackColor = false;
            button1.Click += button1_Click;
            // 
            // label1
            // 
            label1.BackColor = Color.SeaGreen;
            label1.Font = new Font("Segoe UI", 12F);
            label1.ForeColor = SystemColors.Window;
            label1.Location = new Point(69, 213);
            label1.Name = "label1";
            label1.Size = new Size(132, 30);
            label1.TabIndex = 2;
            label1.Text = "Strength: 0";
            // 
            // label2
            // 
            label2.BackColor = Color.SeaGreen;
            label2.Font = new Font("Segoe UI", 12F);
            label2.ForeColor = SystemColors.Window;
            label2.Location = new Point(69, 262);
            label2.Name = "label2";
            label2.Size = new Size(132, 30);
            label2.TabIndex = 3;
            label2.Text = "Charisma: 0";
            // 
            // label3
            // 
            label3.BackColor = Color.SeaGreen;
            label3.Font = new Font("Segoe UI", 12F);
            label3.ForeColor = SystemColors.Window;
            label3.Location = new Point(69, 311);
            label3.Name = "label3";
            label3.Size = new Size(132, 30);
            label3.TabIndex = 4;
            label3.Text = "Intelligence: 0";
            // 
            // label4
            // 
            label4.BackColor = Color.SeaGreen;
            label4.Font = new Font("Segoe UI", 12F);
            label4.ForeColor = SystemColors.Window;
            label4.Location = new Point(69, 361);
            label4.Name = "label4";
            label4.Size = new Size(132, 30);
            label4.TabIndex = 5;
            label4.Text = "Health: 10";
            // 
            // textBox1
            // 
            textBox1.BackColor = Color.IndianRed;
            textBox1.Location = new Point(314, 116);
            textBox1.Multiline = true;
            textBox1.Name = "textBox1";
            textBox1.ScrollBars = ScrollBars.Vertical;
            textBox1.Size = new Size(1009, 427);
            textBox1.TabIndex = 7;
            textBox1.KeyDown += textBox1_KeyDown_1;
            // 
            // button2
            // 
            button2.BackColor = Color.DarkViolet;
            button2.FlatStyle = FlatStyle.Popup;
            button2.Font = new Font("Verdana", 9F, FontStyle.Regular, GraphicsUnit.Point, 162);
            button2.ForeColor = SystemColors.Window;
            button2.Location = new Point(781, 570);
            button2.Name = "button2";
            button2.Size = new Size(94, 29);
            button2.TabIndex = 8;
            button2.Text = "Enter";
            button2.UseVisualStyleBackColor = false;
            button2.Click += button2_Click;
            // 
            // Form2
            // 
            AutoScaleDimensions = new SizeF(8F, 20F);
            AutoScaleMode = AutoScaleMode.Font;
            BackColor = Color.Turquoise;
            ClientSize = new Size(1400, 700);
            Controls.Add(button2);
            Controls.Add(textBox1);
            Controls.Add(label4);
            Controls.Add(label3);
            Controls.Add(label2);
            Controls.Add(label1);
            Controls.Add(button1);
            FormBorderStyle = FormBorderStyle.None;
            Name = "Form2";
            Text = "Form2";
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private Button button1;
        private Label label1;
        private Label label2;
        private Label label3;
        private Label label4;
        private TextBox textBox1;
        private Button button2;
    }
}