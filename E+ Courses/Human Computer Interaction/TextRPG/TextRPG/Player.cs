// Player.cs
// Model class - Contains the player's stats

namespace TextRPG.Models
{
    public class Player
    {
        public int Strength { get; set; }
        public int Charisma { get; set; }
        public int Intelligence { get; set; }
        public int Health { get; set; } = 10;
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
            if (Health > 10) Health = 10; // Maximum health is limited to 10
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