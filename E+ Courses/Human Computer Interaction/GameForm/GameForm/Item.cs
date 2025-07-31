// Item.cs
// Model class - Represents objects in the game

using GameForm.Controller;

namespace GameForm.Models
{
    public class Item
    {
        public string Name { get; }
        public string AffectsStat { get; }
        public int Value { get; }
        public bool IsKeyItem { get; }

        public Item(string name, string affectsStat = "", int value = 0, bool isKeyItem = false)
        {
            Name = name;
            AffectsStat = affectsStat;
            Value = value;
            IsKeyItem = isKeyItem;
        }
    }
}