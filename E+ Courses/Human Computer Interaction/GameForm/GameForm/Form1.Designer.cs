namespace GameForm
{
    partial class Form1
    {
        /// <summary>
        ///  Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        ///  Clean up any resources being used.
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
        ///  Required method for Designer support - do not modify
        ///  the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Form1));
            button2 = new Button();
            button1 = new Button();
            button3 = new Button();
            button5 = new Button();
            button6 = new Button();
            SuspendLayout();
            // 
            // button2
            // 
            button2.BackColor = Color.DarkGray;
            button2.FlatStyle = FlatStyle.Popup;
            button2.Font = new Font("Verdana", 10.8F, FontStyle.Bold);
            button2.ForeColor = Color.Crimson;
            button2.Location = new Point(240, 190);
            button2.Name = "button2";
            button2.Size = new Size(120, 45);
            button2.TabIndex = 2;
            button2.Text = "Settings";
            button2.UseVisualStyleBackColor = false;
            button2.Click += button3_Click;
            // 
            // button1
            // 
            button1.BackColor = Color.DarkRed;
            button1.FlatStyle = FlatStyle.Popup;
            button1.ForeColor = SystemColors.Window;
            button1.Location = new Point(543, 12);
            button1.Name = "button1";
            button1.Size = new Size(45, 45);
            button1.TabIndex = 0;
            button1.Text = "X";
            button1.UseVisualStyleBackColor = false;
            button1.Click += button1_Click;
            // 
            // button3
            // 
            button3.BackColor = Color.DarkGray;
            button3.FlatStyle = FlatStyle.Popup;
            button3.Font = new Font("Verdana", 10.8F, FontStyle.Bold);
            button3.ForeColor = Color.Crimson;
            button3.Location = new Point(240, 110);
            button3.Name = "button3";
            button3.Size = new Size(120, 45);
            button3.TabIndex = 4;
            button3.Text = "Start";
            button3.UseVisualStyleBackColor = false;
            button3.Click += button2_Click;
            // 
            // button5
            // 
            button5.BackColor = Color.DarkGray;
            button5.FlatStyle = FlatStyle.Popup;
            button5.Font = new Font("Verdana", 10.8F, FontStyle.Bold);
            button5.ForeColor = Color.Crimson;
            button5.Location = new Point(240, 270);
            button5.Name = "button5";
            button5.Size = new Size(120, 45);
            button5.TabIndex = 6;
            button5.Text = "About";
            button5.UseVisualStyleBackColor = false;
            button5.Click += button5_Click;
            // 
            // button6
            // 
            button6.BackColor = Color.DarkGray;
            button6.FlatStyle = FlatStyle.Popup;
            button6.Font = new Font("Verdana", 10.8F, FontStyle.Bold);
            button6.ForeColor = Color.Crimson;
            button6.Location = new Point(240, 350);
            button6.Name = "button6";
            button6.Size = new Size(120, 45);
            button6.TabIndex = 7;
            button6.Text = "Credits";
            button6.UseVisualStyleBackColor = false;
            button6.Click += button6_Click;
            // 
            // Form1
            // 
            AutoScaleDimensions = new SizeF(8F, 20F);
            AutoScaleMode = AutoScaleMode.Font;
            BackColor = Color.DarkTurquoise;
            BackgroundImage = (Image)resources.GetObject("$this.BackgroundImage");
            BackgroundImageLayout = ImageLayout.Stretch;
            ClientSize = new Size(600, 490);
            Controls.Add(button6);
            Controls.Add(button5);
            Controls.Add(button3);
            Controls.Add(button2);
            Controls.Add(button1);
            FormBorderStyle = FormBorderStyle.None;
            Name = "Form1";
            Text = "Form1";
            ResumeLayout(false);
        }

        #endregion
        private Button button2;
        private Button button1;
        private Button button3;
        private Button button5;
        private Button button6;
    }
}
