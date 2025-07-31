// GameView.cs
// View class - User interface and input/output operations

using TextRPG.Models;

namespace TextRPG.Views
{
    public static class GameView
    {
        public static void Clear()
        {
            Console.Clear();
        }
        public static void SetColors()
        {
            Console.BackgroundColor = ConsoleColor.DarkBlue;
            Console.ForegroundColor = ConsoleColor.White; 
            Console.Clear();
        }

        public static void SetTextColor(ConsoleColor color)
        {
            Console.ForegroundColor = color;
        }

        public static void ResetColors()
        {
            Console.ResetColor();
        }

        public static void ShowWelcome()
        {
            ConsoleColor[] colors = { ConsoleColor.Red, ConsoleColor.Yellow, ConsoleColor.Green, ConsoleColor.Yellow, ConsoleColor.Magenta, ConsoleColor.Cyan };
            string theMessage = "            ========================================================\r\n            ========== Human-Computer Interaction Project ==========\r\n            ============== made by Mehmet Efe Kapısız ==============\r\n            ========================================================\r\n \r\nYou will control a character who lives an ordinary life in the village of Riverwood.\r\nYour choices will determine the character's fate.\r\nIt is important to make choices according to your stats!\r\n \r\n";
            for (int i = 0; i < theMessage.Length; i++)
            {
                Console.ForegroundColor = colors[i % colors.Length];
                Console.Write(theMessage[i]);
                System.Threading.Thread.Sleep(1);
            }

            PressEnterToContinue();
            ResetColors();
        }

        public static int AskStatAllocation(string statName, int remaining)
        {
            while (true)
            {
                Console.Write($"How many points should be assigned to {statName}? (Remaining: {remaining}): ");
                if (int.TryParse(Console.ReadLine(), out int value))
                {
                    if (value >= 0 && value <= remaining)
                        return value;
                }
                Console.WriteLine($"Invalid value! Enter a number between 0 and {remaining}.");
            }
        }

        public static void TypeText(string text, int delay = 20)
        {
            foreach (char c in text)
            {
                Console.Write(c);
                Thread.Sleep(delay);
            }
            Console.WriteLine();
        }

        public static void ShowStats(Player player)
        {
            Console.BackgroundColor = ConsoleColor.White;
            SetTextColor(ConsoleColor.Green);
            Console.WriteLine("\n---- Character Stats ----");
            Console.WriteLine($"Strength: {player.Strength}");
            Console.WriteLine($"Charisma: {player.Charisma}");
            Console.WriteLine($"Intelligence: {player.Intelligence}");
            Console.WriteLine($"Health: {player.Health}/10");
            Console.WriteLine("-------------------------");
            ShowASCIITitle();
        }
        public static void ShowASCIITitle()
        {
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine(@"
    ╔═══════════════════════════════════════════════════════════════╗
    ║   ███████╗██╗  ██╗ █████╗ ██████╗  ██████╗ ██╗    ██╗         ║
    ║   ██╔════╝██║  ██║██╔══██╗██╔══██╗██╔═══██╗██║    ██║         ║
    ║   ███████╗███████║███████║██║  ██║██║   ██║██║ █╗ ██║         ║
    ║   ╚════██║██╔══██║██╔══██║██║  ██║██║   ██║██║███╗██║         ║
    ║   ███████║██║  ██║██║  ██║██████╔╝╚██████╔╝╚███╔███╔╝         ║
    ║   ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝  ╚═════╝  ╚══╝╚══╝          ║
    ║                                                               ║
    ║          ██╗   ██╗ █████╗ ██╗     ██╗     ███████╗██╗   ██╗   ║
    ║          ██║   ██║██╔══██╗██║     ██║     ██╔════╝╚██╗ ██╔╝   ║
    ║          ██║   ██║███████║██║     ██║     █████╗   ╚████╔╝    ║
    ║          ╚██╗ ██╔╝██╔══██║██║     ██║     ██╔══╝    ╚██╔╝     ║
    ║           ╚████╔╝ ██║  ██║███████╗███████╗███████╗   ██║      ║
    ║            ╚═══╝  ╚═╝  ╚═╝╚══════╝╚══════╝╚══════╝   ╚═╝      ║
    ╚═══════════════════════════════════════════════════════════════╝");

            Console.ForegroundColor = ConsoleColor.DarkCyan;
            Console.WriteLine("Story Begins...");
            Thread.Sleep(2000);
        }

        public static void ShowText(string text, bool slowType = true)
        {
            if (slowType)
                TypeText(text);
            else
                Console.WriteLine(text);

            Console.WriteLine();
        }

        public static string GetChoice(string[] options)
        {
            Console.WriteLine("Options:");
            for (int i = 0; i < options.Length; i++)
                Console.WriteLine($"{i + 1}. {options[i]}");

            int choice;
            while (true)
            {
                Console.Write("Your choice (number): ");
                if (int.TryParse(Console.ReadLine(), out choice) && choice >= 1 && choice <= options.Length)
                    break;

                Console.WriteLine($"Invalid choice! Enter a number between 1 and {options.Length}.");
            }

            return options[choice - 1];
        }

        public static void PressEnterToContinue()
        {
            string text = "Press ENTER to continue...";
            ConsoleColor[] colors = { ConsoleColor.Red, ConsoleColor.Yellow, ConsoleColor.Green, ConsoleColor.Yellow, ConsoleColor.Magenta, ConsoleColor.Cyan };

            Console.WriteLine();

            // Animation
            for (int cycle = 0; cycle < 10; cycle++) // 100 times
            {
                Console.SetCursorPosition(0, Console.CursorTop);

                for (int i = 0; i < text.Length; i++)
                {
                    Console.ForegroundColor = colors[(i + cycle) % colors.Length];
                    Console.Write(text[i]);
                }

                Thread.Sleep(500); // Wait

                // Satırı temizle
                Console.SetCursorPosition(0, Console.CursorTop);
                Console.Write(new string(' ', text.Length));
            }

            // At final, its white
            Console.ForegroundColor = ConsoleColor.White;
            Console.WriteLine(text);
            Console.ReadLine();
            Clear();
        }

        public static void ShowItemFound(Item item)
        {
            if (item.Value > 0)
            {
                Console.WriteLine($"\n=== New Item Found:: {item.Name} ===");
                Console.WriteLine($"Etki: {item.AffectsStat} +{item.Value}");
            }
            else
            {
                Console.WriteLine($"\n=== Key Item Found: {item.Name} ===");
            }
        }

        public static void ShowGameOver(bool victory)
        {
            Clear();
            if (victory)
            {
                Console.WriteLine("=======================================");
                Console.WriteLine("=                                     =");
                Console.WriteLine("=           CONGRATULATIONS!          =");
                Console.WriteLine("=                                     =");
                Console.WriteLine("=======================================");
            }
            else
            {
                Console.WriteLine("=======================================");
                Console.WriteLine("=                                     =");
                Console.WriteLine("=             GAME OVER               =");
                Console.WriteLine("=                                     =");
                Console.WriteLine("=======================================");
                Console.WriteLine("\nYour character did not survive...");
            }

            Console.WriteLine("\nPress any key to close the game...");
            Console.ReadKey();
        }
    }
}