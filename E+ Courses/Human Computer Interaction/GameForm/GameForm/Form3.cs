using GameForm.Controller;

namespace GameForm
{
    public partial class Form3 : Form
    {
        public Form3()
        {
            InitializeComponent();
            checkBoxIronMode.Checked = GameController.IronMode;

            buttonChangeFont.Text = $"Font: {GameController.CurrentFont}";
            buttonChangeColor.Text = $"Color: {GameController.TextColor.Name}";
            numericUpDownFontSize.Value = GameController.FontSize;
        }

        private void Form3_Load(object sender, EventArgs e)
        {
            checkBoxIronMode.Checked = GameController.IronMode;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void textDelayTrackBar_Scroll(object sender, EventArgs e)
        {
            // placeholder
        }

        private void easyModeCheckBox_CheckedChanged(object sender, EventArgs e)
        {
            GameController.EasyMode = easyModeCheckBox.Checked;
        }

        private void checkBoxIronMode_CheckedChanged(object sender, EventArgs e)
        {
            GameController.IronMode = checkBoxIronMode.Checked;
        }

        private void buttonChangeFont_Click(object sender, EventArgs e)
        {
            switch (GameController.CurrentFont)
            {
                case "Segoe UI":
                    GameController.CurrentFont = "Times New Roman";
                    break;
                case "Times New Roman":
                    GameController.CurrentFont = "Comic Sans MS";
                    break;
                case "Comic Sans MS":
                    GameController.CurrentFont = "Segoe UI";
                    break;
                default:
                    GameController.CurrentFont = "Segoe UI";
                    break;
            }

            buttonChangeFont.Text = $"Font: {GameController.CurrentFont}";
        }

        private void buttonChangeColor_Click(object sender, EventArgs e)
        {
            if (GameController.TextColor == Color.Black)
                GameController.TextColor = Color.Blue;
            else if (GameController.TextColor == Color.Blue)
                GameController.TextColor = Color.Green;
            else
                GameController.TextColor = Color.Black;

            buttonChangeColor.Text = $"Color: {GameController.TextColor.Name}";
        }

        private void button3_Click(object sender, EventArgs e)
        {
            GameController.TextDelay = textDelayTrackBar.Value;
            GameController.FontSize = (int)numericUpDownFontSize.Value; // Font size'ı kaydet
        }
    }
}
