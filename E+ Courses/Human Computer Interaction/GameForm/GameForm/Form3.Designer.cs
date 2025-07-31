namespace GameForm
{
    partial class Form3
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
            textDelayTrackBar = new TrackBar();
            label1 = new Label();
            button3 = new Button();
            panel1 = new Panel();
            buttonChangeColor = new Button();
            numericUpDownFontSize = new NumericUpDown();
            label2 = new Label();
            buttonChangeFont = new Button();
            panel2 = new Panel();
            checkBoxIronMode = new CheckBox();
            easyModeCheckBox = new CheckBox();
            pictureBox1 = new PictureBox();
            ((System.ComponentModel.ISupportInitialize)textDelayTrackBar).BeginInit();
            panel1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)numericUpDownFontSize).BeginInit();
            panel2.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)pictureBox1).BeginInit();
            SuspendLayout();
            // 
            // button1
            // 
            button1.BackColor = Color.DarkRed;
            button1.FlatStyle = FlatStyle.Popup;
            button1.ForeColor = SystemColors.Window;
            button1.Location = new Point(743, 12);
            button1.Name = "button1";
            button1.Size = new Size(45, 45);
            button1.TabIndex = 1;
            button1.Text = "X";
            button1.UseVisualStyleBackColor = false;
            button1.Click += button1_Click;
            // 
            // textDelayTrackBar
            // 
            textDelayTrackBar.AutoSize = false;
            textDelayTrackBar.Location = new Point(120, 23);
            textDelayTrackBar.Name = "textDelayTrackBar";
            textDelayTrackBar.Size = new Size(115, 21);
            textDelayTrackBar.TabIndex = 2;
            textDelayTrackBar.Scroll += textDelayTrackBar_Scroll;
            // 
            // label1
            // 
            label1.BackColor = Color.Transparent;
            label1.Font = new Font("Segoe UI", 9F, FontStyle.Regular, GraphicsUnit.Point, 162);
            label1.ForeColor = SystemColors.Window;
            label1.Location = new Point(18, 23);
            label1.Name = "label1";
            label1.Size = new Size(93, 21);
            label1.TabIndex = 3;
            label1.Text = "Text Speed";
            label1.TextAlign = ContentAlignment.TopCenter;
            // 
            // button3
            // 
            button3.BackColor = Color.Gold;
            button3.FlatStyle = FlatStyle.Popup;
            button3.Font = new Font("Verdana", 10.2F, FontStyle.Bold);
            button3.ForeColor = Color.DeepPink;
            button3.Location = new Point(3, 99);
            button3.Name = "button3";
            button3.Size = new Size(244, 34);
            button3.TabIndex = 5;
            button3.Text = "Save";
            button3.UseVisualStyleBackColor = false;
            button3.Click += button3_Click;
            // 
            // panel1
            // 
            panel1.BackColor = Color.IndianRed;
            panel1.Controls.Add(numericUpDownFontSize);
            panel1.Controls.Add(label2);
            panel1.Controls.Add(buttonChangeColor);
            panel1.Controls.Add(button3);
            panel1.Controls.Add(buttonChangeFont);
            panel1.Controls.Add(textDelayTrackBar);
            panel1.Controls.Add(label1);
            panel1.Location = new Point(50, 46);
            panel1.Name = "panel1";
            panel1.Size = new Size(250, 350);
            panel1.TabIndex = 6;
            // 
            // buttonChangeColor
            // 
            buttonChangeColor.BackColor = Color.Gray;
            buttonChangeColor.FlatStyle = FlatStyle.Popup;
            buttonChangeColor.Font = new Font("Verdana", 10.2F, FontStyle.Bold);
            buttonChangeColor.ForeColor = Color.Aqua;
            buttonChangeColor.Location = new Point(18, 298);
            buttonChangeColor.Name = "buttonChangeColor";
            buttonChangeColor.Size = new Size(217, 34);
            buttonChangeColor.TabIndex = 9;
            buttonChangeColor.Text = "Change Text Color";
            buttonChangeColor.UseVisualStyleBackColor = false;
            buttonChangeColor.Click += buttonChangeColor_Click;
            // 
            // numericUpDownFontSize
            // 
            numericUpDownFontSize.Location = new Point(120, 55);
            numericUpDownFontSize.Maximum = new decimal(new int[] { 72, 0, 0, 0 });
            numericUpDownFontSize.Minimum = new decimal(new int[] { 8, 0, 0, 0 });
            numericUpDownFontSize.Name = "numericUpDownFontSize";
            numericUpDownFontSize.Size = new Size(53, 27);
            numericUpDownFontSize.TabIndex = 7;
            numericUpDownFontSize.Value = new decimal(new int[] { 12, 0, 0, 0 });
            // 
            // label2
            // 
            label2.Font = new Font("Segoe UI", 9F, FontStyle.Regular, GraphicsUnit.Point, 162);
            label2.ForeColor = SystemColors.Window;
            label2.Location = new Point(18, 57);
            label2.Name = "label2";
            label2.Size = new Size(93, 22);
            label2.TabIndex = 8;
            label2.Text = "Font Size:\n";
            // 
            // buttonChangeFont
            // 
            buttonChangeFont.BackColor = Color.SeaGreen;
            buttonChangeFont.FlatStyle = FlatStyle.Popup;
            buttonChangeFont.Font = new Font("Verdana", 10.2F, FontStyle.Bold);
            buttonChangeFont.ForeColor = Color.Violet;
            buttonChangeFont.Location = new Point(18, 216);
            buttonChangeFont.Name = "buttonChangeFont";
            buttonChangeFont.Size = new Size(217, 34);
            buttonChangeFont.TabIndex = 6;
            buttonChangeFont.Text = "Change Font";
            buttonChangeFont.UseVisualStyleBackColor = false;
            buttonChangeFont.Click += buttonChangeFont_Click;
            // 
            // panel2
            // 
            panel2.Controls.Add(checkBoxIronMode);
            panel2.Controls.Add(easyModeCheckBox);
            panel2.Location = new Point(500, 120);
            panel2.Name = "panel2";
            panel2.Size = new Size(258, 200);
            panel2.TabIndex = 7;
            // 
            // checkBoxIronMode
            // 
            checkBoxIronMode.AutoSize = true;
            checkBoxIronMode.Font = new Font("Segoe UI", 9F, FontStyle.Bold);
            checkBoxIronMode.ForeColor = SystemColors.Window;
            checkBoxIronMode.Location = new Point(3, 121);
            checkBoxIronMode.Name = "checkBoxIronMode";
            checkBoxIronMode.Size = new Size(179, 24);
            checkBoxIronMode.TabIndex = 1;
            checkBoxIronMode.Text = "Iron Mode (1 Health)";
            checkBoxIronMode.UseVisualStyleBackColor = true;
            checkBoxIronMode.CheckedChanged += checkBoxIronMode_CheckedChanged;
            // 
            // easyModeCheckBox
            // 
            easyModeCheckBox.AutoSize = true;
            easyModeCheckBox.Font = new Font("Segoe UI", 9F, FontStyle.Bold);
            easyModeCheckBox.ForeColor = SystemColors.Window;
            easyModeCheckBox.Location = new Point(3, 65);
            easyModeCheckBox.Name = "easyModeCheckBox";
            easyModeCheckBox.Size = new Size(256, 24);
            easyModeCheckBox.TabIndex = 0;
            easyModeCheckBox.Text = "Easy Mode (7 stats instead of 5)";
            easyModeCheckBox.UseVisualStyleBackColor = true;
            easyModeCheckBox.CheckedChanged += easyModeCheckBox_CheckedChanged;
            // 
            // pictureBox1
            // 
            pictureBox1.Image = Properties.Resources.laura_pennington_sword_1;
            pictureBox1.Location = new Point(-1, 0);
            pictureBox1.Name = "pictureBox1";
            pictureBox1.Size = new Size(802, 451);
            pictureBox1.SizeMode = PictureBoxSizeMode.StretchImage;
            pictureBox1.TabIndex = 8;
            pictureBox1.TabStop = false;
            // 
            // Form3
            // 
            AutoScaleDimensions = new SizeF(8F, 20F);
            AutoScaleMode = AutoScaleMode.Font;
            BackColor = Color.IndianRed;
            ClientSize = new Size(800, 450);
            Controls.Add(panel2);
            Controls.Add(panel1);
            Controls.Add(button1);
            Controls.Add(pictureBox1);
            FormBorderStyle = FormBorderStyle.None;
            Name = "Form3";
            Text = "Form3";
            ((System.ComponentModel.ISupportInitialize)textDelayTrackBar).EndInit();
            panel1.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)numericUpDownFontSize).EndInit();
            panel2.ResumeLayout(false);
            panel2.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)pictureBox1).EndInit();
            ResumeLayout(false);
        }

        #endregion

        private Button button1;
        private TrackBar textDelayTrackBar;
        private Label label1;
        private Button button3;
        private Panel panel1;
        private Panel panel2;
        private CheckBox easyModeCheckBox;
        private PictureBox pictureBox1;
        private CheckBox checkBoxIronMode;
        private NumericUpDown numericUpDownFontSize;
        private Label label2;
        private Button buttonChangeFont;
        private Button buttonChangeColor;
    }
}