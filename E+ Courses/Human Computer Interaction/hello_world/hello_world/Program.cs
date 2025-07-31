namespace hello_world
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.Title = "Hello WORLD!";

            Console.Clear();
            Console.BackgroundColor = ConsoleColor.Gray;
            Console.Clear();

            Console.ForegroundColor = ConsoleColor.Red;
            Console.Write("Hello World!");

            Console.WriteLine();

            ConsoleColor[] colors = { ConsoleColor.Red, ConsoleColor.Yellow, ConsoleColor.Green, ConsoleColor.Cyan, ConsoleColor.Blue, ConsoleColor.Magenta };
            string exitMessage = "Press any button the close the program...";
            for (int i = 0; i < exitMessage.Length; i++)
            {
                Console.ForegroundColor = colors[i % colors.Length];
                Console.Write(exitMessage[i]);
                System.Threading.Thread.Sleep(50);
            }

            Console.ReadKey();
            Console.ForegroundColor = ConsoleColor.Green;
            Console.WriteLine("\nProgram is closing!");
            Console.ResetColor();
        }
    }
}