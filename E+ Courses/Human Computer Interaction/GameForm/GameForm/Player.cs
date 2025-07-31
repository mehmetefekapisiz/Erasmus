// Player.cs
// Model class - Contains the player's stats

using GameForm.Controller;

namespace GameForm.Models
{
    public class Player
    {
        public int Strength { get; set; }
        public int Charisma { get; set; }
        public int Intelligence { get; set; }
        public int Health { get; set; } = GameController.IronMode ? 1 : 10;
        public Inventory Inventory { get; set; } = new Inventory();

        public bool IsAlive => Health > 0;

        public void ApplyDamage(int damage)
        {
            Health -= damage;
            if (Health < 0) Health = 0;
        }

        public void Heal(int amount)
        {
            Health += amount;
            int maxHealth = GameController.IronMode ? 1 : 10;
            if (Health > maxHealth) Health = maxHealth;
        }

        public void AddStat(string stat, int amount)
        {
            switch (stat.ToLower())
            {
                case "strength":
                    Strength += amount;
                    break;
                case "charisma":
                    Charisma += amount;
                    break;
                case "intelligence":
                    Intelligence += amount;
                    break;
            }
        }

        public bool CheckStat(string stat, int required)
        {
            return stat.ToLower() switch
            {
                "strength" => Strength >= required,
                "charisma" => Charisma >= required,
                "intelligence" => Intelligence >= required,
                _ => false
            };
        }
    }
}