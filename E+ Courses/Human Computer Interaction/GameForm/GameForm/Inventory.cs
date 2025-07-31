// Inventory.cs
// Model class - Contains the player's inventory and transactions

namespace GameForm.Models
{
    public class Inventory
    {
        private List<Item> items = new List<Item>();
        public List<Item> Items => items;
        private Form2 gameForm;

        public void AddItem(Item item)
        {
            items.Add(item);
            gameForm.AppendTextToRichBox($"You have acquired: \"{item.Name}\".");
        }

        public bool HasItem(string itemName)
        {
            return items.Exists(item => item.Name.Equals(itemName, StringComparison.OrdinalIgnoreCase));
        }

        public void ShowInventory()
        {
            if (items.Count == 0)
            {
                gameForm.AppendTextToRichBox("Your inventory is empty.");
                return;
            }

            gameForm.AppendTextToRichBox("Inventory:");
            foreach (var item in items)
            {
                gameForm.AppendTextToRichBox($"- {item.Name}" + (item.Value > 0 ? $" ({item.AffectsStat} +{item.Value})" : ""));
            }
        }
    }
}