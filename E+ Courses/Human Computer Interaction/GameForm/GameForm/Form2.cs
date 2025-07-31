using GameForm.Models;
using GameForm.Controller;

namespace GameForm
{
    public partial class Form2 : Form
    {
        private Player _currentPlayer;
        private GameController gameController;
        private bool waitingForInput = false;
        private string inputPrompt = "";


        public Form2(Player player)
        {
            InitializeComponent();
            _currentPlayer = player;
            UpdatePlayerStatsDisplay();
            ApplyFontSettings();

            gameController = new GameController(this);
            this.Shown += Form2_Shown;
        }

        private void Form2_Shown(object sender, EventArgs e)
        {
            Task.Run(() => gameController.StartGame());
        }

        private void Form2_Load(object sender, EventArgs e)
        {
            gameController.StartGame();
        }

        public void UpdatePlayerStatsDisplay()
        {
            if (_currentPlayer != null)
            {
                if (InvokeRequired)
                {
                    Invoke(new Action(() =>
                    {
                        label1.Text = $"Strength: {_currentPlayer.Strength}";
                        label2.Text = $"Charisma: {_currentPlayer.Charisma}";
                        label3.Text = $"Intelligence: {_currentPlayer.Intelligence}";
                        label4.Text = $"Health: {_currentPlayer.Health}";
                    }));
                }
                else
                {
                    label1.Text = $"Strength: {_currentPlayer.Strength}";
                    label2.Text = $"Charisma: {_currentPlayer.Charisma}";
                    label3.Text = $"Intelligence: {_currentPlayer.Intelligence}";
                    label4.Text = $"Health: {_currentPlayer.Health}";
                }
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            string[] lines = textBox1.Text.Split(new[] { '\r', '\n' }, StringSplitOptions.RemoveEmptyEntries);
            if (lines.Length > 0)
            {
                string lastLine = lines[lines.Length - 1].Trim();
                string[] words = lastLine.Split(' ');
                lastUserInput = words[words.Length - 1].Trim();
            }

            inputReceived = true;
        }
        private void button1_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void textBox1_KeyDown_1(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Enter)
            {
                button2_Click(sender, e);
                e.Handled = true;
                e.SuppressKeyPress = true;
            }
        }

        public void AppendTextToRichBox(string text)
        {
            if (textBox1.InvokeRequired)
            {
                textBox1.Invoke(new Action(() =>
                {
                    textBox1.Font = new Font(GameController.CurrentFont, GameController.FontSize);
                    textBox1.ForeColor = GameController.TextColor;

                    textBox1.AppendText(text + Environment.NewLine);
                    textBox1.SelectionStart = textBox1.Text.Length;
                    textBox1.ScrollToCaret();
                }));
            }
            else
            {
                textBox1.Font = new Font(GameController.CurrentFont, GameController.FontSize);
                textBox1.ForeColor = GameController.TextColor;

                textBox1.AppendText(text + Environment.NewLine);
                textBox1.SelectionStart = textBox1.Text.Length;
                textBox1.ScrollToCaret();
            }
        }

        public void ClearRichBox()
        {
            if (textBox1.InvokeRequired)
            {
                textBox1.Invoke(new Action(() => textBox1.Clear()));
            }
            else
            {
                textBox1.Clear();
            }
        }

        private string lastUserInput = "";
        private bool inputReceived = false;

        public string GetUserInput()
        {
            inputReceived = false;

            while (!inputReceived)
            {
                Thread.Sleep(100);
                Application.DoEvents();
            }

            return lastUserInput;
        }

        public void SetCurrentPlayer(Player player)
        {
            _currentPlayer = player;
            UpdatePlayerStatsDisplay();
        }
        public void ApplyFontSettings()
        {
            if (textBox1.InvokeRequired)
            {
                textBox1.Invoke(new Action(() =>
                {
                    textBox1.Font = new Font(GameController.CurrentFont, GameController.FontSize);
                    textBox1.ForeColor = GameController.TextColor;
                }));
            }
            else
            {
                textBox1.Font = new Font(GameController.CurrentFont, GameController.FontSize);
                textBox1.ForeColor = GameController.TextColor;
            }
        }
    }
}