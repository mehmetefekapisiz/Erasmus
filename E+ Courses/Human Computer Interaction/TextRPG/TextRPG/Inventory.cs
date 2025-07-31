// Inventory.cs
// Model class - Contains the player's inventory and transactions

namespace TextRPG.Models
{
    public class Inventory
    {
        private List<Item> items = new List<Item>();

        public List<Item> Items => items;

        public void AddItem(Item item)
        {
            items.Add(item);
            Console.WriteLine($"You have acquired: \"{item.Name}\".");
        }

        public bool HasItem(string itemName)
        {
            return items.Exists(item => item.Name.Equals(itemName, StringComparison.OrdinalIgnoreCase));
        }

        public void ShowInventory()
        {
            if (items.Count == 0)
            {
                Console.WriteLine("Your inventory is empty.");
                return;
            }

            Console.WriteLine("Inventory:");
            foreach (var item in items)
            {
                Console.WriteLine($"- {item.Name}" + (item.Value > 0 ? $" ({item.AffectsStat} +{item.Value})" : ""));
            }
        }
    }
}