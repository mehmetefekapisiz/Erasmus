// Program.cs
// Main program entry point

using TextRPG.Controllers;

namespace TextRPG
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.Title = "Human-Computer Interaction Project";

            Console.SetWindowSize(120, 30);
            Console.SetBufferSize(120, 30);

            Console.BackgroundColor = ConsoleColor.DarkBlue;

            GameController controller = new GameController();
            controller.StartGame();
        }
    }
}