// GameController.cs
// Controller class - Manages the flow and logic of the game

using GameForm.Models;

namespace GameForm.Controller
{
    public class GameController
    {
        private Player player;
        private bool gameWon = false;
        private string ending = ""; // To indicate which ending the game ends with.

        public static int TextDelay = 1;
        public static bool EasyMode = false;
        public static bool IronMode = false;

        public static string CurrentFont = "Segoe UI";
        public static int FontSize = 12;
        public static Color TextColor = Color.Black;

        private Form2 gameForm;

        public GameController(Form2 form)
        {
            gameForm = form;
            gameForm.SetCurrentPlayer(player);
        }

        public void StartGame()
        {
            gameForm.AppendTextToRichBox("OYUN BAŞLADI!");
            ShowWelcome();
            InitializePlayer();
            
            Introduction();

            if (!player.IsAlive)
                ShowGameOver(false);
            else if (gameWon)
                ShowGameOver(true);
        }
        public void ProcessUserInput(string input)
        {
            // Placeholder
        }
        public void ShowGameOver(bool victory)
        {
            if (victory)
            {
                gameForm.AppendTextToRichBox("=======================================");
                gameForm.AppendTextToRichBox("=                                     =");
                gameForm.AppendTextToRichBox("=           CONGRATULATIONS!          =");
                gameForm.AppendTextToRichBox("=                                     =");
                gameForm.AppendTextToRichBox("=======================================");
            }
            else
            {
                gameForm.AppendTextToRichBox("=======================================");
                gameForm.AppendTextToRichBox("=                                     =");
                gameForm.AppendTextToRichBox("=             GAME OVER               =");
                gameForm.AppendTextToRichBox("=                                     =");
                gameForm.AppendTextToRichBox("=======================================");
                gameForm.AppendTextToRichBox("\nYour character did not survive...");
            }

            gameForm.AppendTextToRichBox("\nPress any key to close the game...");
        }

        public void ShowWelcome()
        {
            gameForm.AppendTextToRichBox("========================================================");
            gameForm.AppendTextToRichBox("========== Human-Computer Interaction Project ==========");
            gameForm.AppendTextToRichBox("============== made by Mehmet Efe Kapısız ==============");
            gameForm.AppendTextToRichBox("========================================================");
            gameForm.AppendTextToRichBox("\nYou will control a character who lives an ordinary life in the village of Riverwood.");
            gameForm.AppendTextToRichBox("Your choices will determine the character's fate.");
            gameForm.AppendTextToRichBox("It is important to make choices according to your stats!");
            gameForm.AppendTextToRichBox("\nPress any key to get started...");
        }

        public int AskStatAllocation(string statName, int remaining)
        {
            gameForm.AppendTextToRichBox($"How many points should be assigned to {statName}? (Remaining: {remaining}): ");

            while (true)
            {
                string input = gameForm.GetUserInput();
                gameForm.AppendTextToRichBox($"[DEBUG] Trying to parse: '{input}'");

                if (!string.IsNullOrWhiteSpace(input) && int.TryParse(input, out int value))
                {
                    if (value >= 0 && value <= remaining)
                        return value;
                }
                gameForm.AppendTextToRichBox($"Invalid value! Enter a number between 0 and {remaining}.");
                gameForm.AppendTextToRichBox($"How many points should be assigned to {statName}? (Remaining: {remaining}): ");
            }
        }

        public void TypeText(string text, int delay = 1)
        {
            foreach (char c in text)
            {
                gameForm.AppendTextToRichBox(text);
                Thread.Sleep(TextDelay);
            }
            gameForm.AppendTextToRichBox(text);
        }

        public void ShowStats(Player player)
        {
            gameForm.AppendTextToRichBox("\n---- Character Stats ----");
            gameForm.AppendTextToRichBox($"Strength: {player.Strength}");
            gameForm.AppendTextToRichBox($"Charisma: {player.Charisma}");
            gameForm.AppendTextToRichBox($"Intelligence: {player.Intelligence}");
            gameForm.AppendTextToRichBox($"Health: {player.Health}/10");
            gameForm.AppendTextToRichBox("-------------------------");
        }

        public void ShowText(string text, bool slowType = true)
        {
            if (slowType)
                TypeText(text);
            else
                gameForm.AppendTextToRichBox(text);

            gameForm.AppendTextToRichBox("");
        }

        public void Clear()
        {
            gameForm.ClearRichBox();
        }

        public void PressEnterToContinue()
        {
            gameForm.AppendTextToRichBox("\nPress ENTER to continue...");
            gameForm.GetUserInput();
        }

        public string GetChoice(string[] options)
        {
            gameForm.AppendTextToRichBox("Options:");
            for (int i = 0; i < options.Length; i++)
                gameForm.AppendTextToRichBox($"{i + 1}. {options[i]}");

            int choice;
            while (true)
            {
                gameForm.AppendTextToRichBox("Your choice (number): ");
                if (int.TryParse(gameForm.GetUserInput(), out choice) && choice >= 1 && choice <= options.Length)
                    break;

                gameForm.AppendTextToRichBox($"Invalid choice! Enter a number between 1 and {options.Length}.");
            }

            return options[choice - 1];
        }

        public void ShowItemFound(Item item)
        {
            if (item.Value > 0)
            {
                gameForm.AppendTextToRichBox($"\n=== New Item Found:: {item.Name} ===");
                gameForm.AppendTextToRichBox($"Etki: {item.AffectsStat} +{item.Value}");
            }
            else
            {
                gameForm.AppendTextToRichBox($"\n=== Key Item Found: {item.Name} ===");
            }
        }
        private void InitializePlayer()
        {
            player = new Player();
            gameForm.SetCurrentPlayer(player);
            int totalPoints = GameController.EasyMode ? 7 : 5;
            int remainingPoints = totalPoints;

            ShowText("SECRET OF THE SHADOW VALLEY", false);
            ShowText($"Let's start creating your character! You have {totalPoints} points to distribute between Strength, Charisma, and Intelligence.", false);

            int strength = AskStatAllocation("Strength", remainingPoints);
            remainingPoints -= strength;

            int charisma = AskStatAllocation("Charisma", remainingPoints);
            remainingPoints -= charisma;

            int intelligence = remainingPoints;

            player.Strength = strength;
            player.Charisma = charisma;
            player.Intelligence = intelligence;

            ShowText("Your character has been created!", false);
            ShowStats(player);
            gameForm.UpdatePlayerStatsDisplay();
            PressEnterToContinue();
        }

        private void Introduction()
        {
            ShowText("As the sun rises over the village of Riverwood, you look out the window of your house.");
            ShowText("There have been unusual events in the village in recent weeks; people have been disappearing, strange noises have been heard in the fields at night, and the animals in the houses near the forest are agitated.");
            ShowText("This morning, the Village Headman called all the villagers to gather in the square.");

            var choice = GetChoice(new[] {
                "Go to the village square immediately.",
                "Arrange your recently deceased grandfather's belongings. (Intelligence 2+ required)",
                "Go talk to your neighbor (Charisma 2+ required)"
            });

            if (choice == "Go to the village square immediately.")
            {
                ShowText("You quickly get ready and set off towards the village square.");
                ShowText("The other villagers have started to gather, everyone looks worried.");
                VillageMeeting();
            }
            else if (choice == "Arrange your recently deceased grandfather's belongings. (Intelligence 2+ required)")
            {
                if (player.CheckStat("intelligence", 2))
                {
                    ShowText("Your clever eyes notice a secret compartment behind an old chest.");
                    ShowText("When you open it, you find your grandfather's old journal.");
                    ShowText("The journal contains information about the Shadow Valley. There are notes describing events that happened in the valley in the old days.");

                    Item diary = new Item("Grandfather's Diary", "intelligence", 1);
                    player.Inventory.AddItem(diary);
                    player.Inventory.ShowInventory();
                    player.AddStat(diary.AffectsStat, diary.Value);
                    ShowItemFound(diary);
                    ShowStats(player);

                    ShowText("You take the diary with you and go to the village square.");
                    VillageMeeting();
                }
                else
                {
                    ShowText("You spent time at home but couldn't find anything important. You wasted time.");
                    ShowText("When you reach the village square, you realize you missed part of the Village Headman's speech.");
                    VillageMeeting(true);
                }
            }
            else // Go talk to your neighbor (Charisma 2+ required)
            {
                if (player.CheckStat("charisma", 2))
                {
                    ShowText("Before you go to the village square, you stop by your neighbor Marika.");
                    ShowText("\"Oh, good morning! I was also going to the square. I couldn't sleep last night, did you hear those strange noises?\"");
                    ShowText("Marika gives you detailed information about the missing people:");
                    ShowText("\"The last person to go missing was Blacksmith Hewg. He went missing while smelting his iron near the Shadow Valley. But no one dares to go to that area anymore.\"");
                    ShowText("With this information in mind, you go to the village square together.");
                    VillageMeeting();
                }
                else
                {
                    ShowText("Your neighbor Marika opens the door and quickly closes it when she sees you.");
                    ShowText("\"Sorry, I'm very busy right now. Maybe I'll see you in the square.\"");
                    ShowText("After your neighbor's strange behavior, you lost time and are late for the village square.");
                    VillageMeeting(true);
                }
            }
        }

        private void VillageMeeting(bool late = false)
        {
            if (late)
            {
                ShowText("As you arrive at the square, you see that Headman Adrian is in the middle of his speech:");
                ShowText("\"...and last night Forest Ranger Henry's cabin was attacked. Henry says the attackers looked more like creatures than humans.\"");
            }
            else
            {
                ShowText("In the village square, Headman Adrian addresses the crowd:");
                ShowText("\"As you all know, our village is in danger. Five people have gone missing in the past month, and last night Forest Ranger Henry's cabin was attacked.\"");
                ShowText("\"Henry says the attackers looked more like creatures than humans. They might have come from Shadow Valley.\"");
            }

            ShowText("\"I am looking for volunteers to help protect the village.\"");

            var choice = GetChoice(new[] {
                "Volunteer and say you want to help.",
                "Wait to see what the other villagers do.",
                "Step forward and explain why you're strong. (Requires Strength 3+)"
    });

            if (choice == "Volunteer and say you want to help.")
            {
                ShowText("You step forward and say, \"I'll help, Headman Adrian!\"");
                ShowText("Adrian smiles with gratitude: \"You are brave. We need your help.\"");
                FirstTask();
            }
            else if (choice == "Wait to see what the other villagers do.")
            {
                ShowText("You stand silently and wait for others to respond.");
                ShowText("The village strongman Gareth suddenly points at you:");
                ShowText("\"Look at that! Hiding in a moment of danger. It's cowards like this that put our village at risk!\"");
                ShowText("The villagers look at you with suspicion. Your charisma has decreased!");

                player.AddStat("charisma", -1);
                ShowStats(player);

                ShowText("Ashamed, you finally decide to volunteer.");
                FirstTask();
            }
            else // Step forward (Requires Strength 3+)
            {
                if (player.CheckStat("strength", 3))
                {
                    ShowText("You step in front of the crowd and show off your strong arms.");
                    ShowText("\"Look, I'm one of the strongest people in this village. If someone has to go to Shadow Valley, it should be me!\"");
                    ShowText("The villagers are impressed and nod respectfully. Headman Adrian looks at you with confidence.");
                    ShowText("\"We will need your strength. Thank you!\"");
                    FirstTask();
                }
                else
                {
                    ShowText("You step in front of the crowd and puff your chest, but your voice and posture aren't very convincing.");
                    ShowText("You say, \"I... I'm strong and I-I can help!\"");
                    ShowText("The village strongman Gareth laughs: \"You? You can't even carry a chicken!\"");
                    ShowText("The villagers chuckle and you feel humiliated. You've lost reputation and feel bad.");

                    player.ApplyDamage(1);
                    ShowStats(player);

                    ShowText("Under everyone's gaze, you reluctantly volunteer.");
                    FirstTask();
                }
            }
        }

        private void FirstTask()
        {
            PressEnterToContinue();
            ShowText("Headman Adrian gives the volunteers their first task:");
            ShowText("\"We need to form an exploration team to track down the missing people on the way to Shadow Valley.\"");
            ShowText("\"We also need to build defensive measures around the village.\"");

            var choices = new string[] {
                "Join the exploration team.",
                "Help defend the village."
            };

            var choice = GetChoice(choices);

            if (choice == "Join the exploration team.")
            {
                ShowText("\"I want to join the expedition team,\" you say. \"To track down missing people and explore the Shadow Valley.\"");
                ShowText("Headman Adrian nods. \"Good. I'll assign three more people to accompany you. Be careful.\"");
                ExpeditionPath();
            }
            else if (choice == "Help defend the village.")
            {
                ShowText("\"I want to help defend the village,\" you say. \"We must protect our homes.\"");
                ShowText("Headman Adrian nods. \"A wise decision. We must strengthen our defensive measures.\"");
                VillageDefensePath();
            }
        }

        private void ExpeditionPath()
        {
            PressEnterToContinue();
            ForestEncounter();
        }

        private void ForestEncounter()
        {
            ShowText("As you and three other villagers head towards the Shadow Valley, you hear rustling sounds coming from the trees. The forest is getting darker.");
            ShowText("Suddenly, a dark shadow jumps out from behind a tree and blocks your path! With one blow, it kills one of the villagers next to you. The other villagers run deeper into the forest. You face the creature alone!");

            var choice = GetChoice(new[] {
                "Attack. (Strength 3+ required)",
                "Escape.",
                "Try to communicate. (Charisma 3+ required)"
            });

            if (choice == "Attack. (Strength 3+ required)")
            {
                if (player.CheckStat("strength", 3))
                {
                    ShowText("You attack the shadow with a quick move!");
                    ShowText("After a short fight, you defeat the shadow creature.");
                    ShowText("As the creature runs away, it drops a shining necklace from its neck.");

                    Item necklace = new Item("Mysterious Necklace", "charisma", 1);
                    player.Inventory.AddItem(necklace);
                    player.Inventory.ShowInventory();
                    player.AddStat(necklace.AffectsStat, necklace.Value);
                    ShowItemFound(necklace);
                    ShowStats(player);

                    ShowText("You put the necklace around your neck.");
                    DeepForest();
                }
                else
                {
                    ShowText("You lunge at the shadow, but the creature is too fast and strong!");
                    ShowText("It hits you and knocks you to the ground!");

                    player.ApplyDamage(2);
                    ShowStats(player);

                    if (!player.IsAlive)
                    {
                        ShowText("Your injuries are severe. You are on your last breath.");
                        return;
                    }

                    ShowText("You are barely standing. The creature went deep into the forest to chase other villagers.");
                    DeepForest();
                }
            }
            else if (choice == "Escape.")
            {
                ShowText("You think you can't do anything and you run away without even looking back.");
                ShowText("You're lucky. The creature went after the other villagers and got away from you.");
                DeepForest();
            }
            else // Try to communicate. (Charisma 3+ required)
            {
                if (player.CheckStat("charisma", 3))
                {
                    ShowText("You raise your hands and speak in a calm voice: \"I don't want to hurt you. Tell us who you are.\"");
                    ShowText("The shadow is slowly taking on a human form. It's Lena, the villager who disappeared a week ago!");
                    ShowText("\"Help... me...\" she says in a weak voice. \"The temple... is changing us...\"");
                    ShowText("Lena is talking about the temple in the Shadow Valley and the dark power within it.");
                    ShowText("\"The shaman... is gathering people... to feed the crystal...\"");
                    ShowText("You are committing this valuable information to your mind. After a while, Lena turns back into a shadow and runs away into the depths of the forest.");
                    DeepForest();
                }
                else
                {
                    ShowText("\"Hey, stop! We just want to talk!\" you try to say, but your words are ineffective.");
                    ShowText("The shadow growls and lunges at you! Its sharp claws stab your arm. You fall to the ground.");
                    ShowText("You're lucky. The creature followed the other villagers and got away from you.");

                    player.ApplyDamage(1);
                    ShowStats(player);
                    DeepForest();
                }
            }
        }

        private void DeepForest()
        {
            PressEnterToContinue();
            ShowText("As you go deeper into the forest, you come across an old stone structure.");
            ShowText("Its door is locked and has strange symbols on it. This could be the forgotten entrance to an ancient temple.");

            var choice = GetChoice(new[] {
                "Force the door (Strength 4+ required)",
                "Examine the symbols (Intelligence 3+ required)",
                "Look around for the key."
            });

            if (choice == "Force the door (Strength 4+ required)")
            {
                if (player.CheckStat("strength", 4))
                {
                    ShowText("You gather all your strength and charge at the door. The old stone door cracks open!");
                    ShowText("Inside, you see an ancient sword hanging on the wall. You pick it up.");

                    Item sword = new Item("Ancient Sword", "strength", 1);
                    player.Inventory.AddItem(sword);
                    player.Inventory.ShowInventory();
                    player.AddStat(sword.AffectsStat, sword.Value);
                    ShowItemFound(sword);
                    ShowStats(player);

                    ShowText("You wield the sword and move deeper into the ancient temple.");
                    TempleEntrance();
                }
                else
                {
                    ShowText("You use all your strength to push the door, but it doesn't budge.");
                    ShowText("As you continue to hit the door with your shoulder, a mechanism suddenly triggers!");
                    ShowText("A small arrow flies out of the door and hits your shoulder. You jump back in pain!");

                    player.ApplyDamage(2);
                    ShowStats(player);

                    if (!player.IsAlive)
                    {
                        ShowText("The arrow hits you so deep! There's nothing to do. You take your last breath.");
                        return;
                    }

                    ShowText("You have to try other ways.");
                    DeepForest(); // Make the choice again.
                }
            }
            else if (choice == "Examine the symbols (Intelligence 3+ required)")
            {
                if (player.CheckStat("intelligence", 3))
                {
                    ShowText("You study the symbols carefully. They are from a very old language, but you recognize some patterns.");
                    ShowText("You notice that there are symbols around the door that must be touched in a certain order.");
                    ShowText("When you touch the symbols in the correct order, the door opens silently!");
                    ShowText("Thanks to your intelligence, you have managed to enter the temple silently.");
                    TempleEntrance();
                }
                else
                {
                    ShowText("You look at the symbols but can't make any sense of them. The symbols you touch suddenly turn red!");
                    ShowText("A gas belches out of the door and you have to breathe it in. You cough and your head spins.");

                    player.ApplyDamage(2);
                    ShowStats(player);

                    ShowText("After the gas has dissipated, you decide to try other options.");
                    DeepForest(); // Make the choice again.
                }
            }
            else // Look around for the key.
            {
                ShowText("You start looking around for a key.");
                ShowText("After a careful search, you find a box hidden in a nearby tree hole.");
                ShowText("As you open the box, a snake jumps out and stabs you in the arm!");

                player.ApplyDamage(2);
                ShowStats(player);

                ShowText("You panic and throw yourself at the snake and eventually get rid of it. A key rolls out of the box that fell to the ground.");

                Item key = new Item("Ancient Key", isKeyItem: true);
                player.Inventory.AddItem(key);
                player.Inventory.ShowInventory();
                ShowItemFound(key);

                ShowText("When you put the key in the lock of the door, the door opens silently.");
                ShowText("You acted wisely and managed to enter the temple safely.");
                TempleEntrance();
            }
        }

        private void TempleEntrance()
        {
            PressEnterToContinue();
            ShowText("You step inside the temple. You walk down a dimly lit corridor.");
            ShowText("The walls are covered with ancient drawings, and there is a mysterious light streaming down the floor.");
            ShowText("The corridor extends deeper into the temple.");
            ShowText("It looks like you'll have a hard time finding your way inside, as you've entered through a forgotten door of the ancient temple.");

            var choice = GetChoice(new[] {
                "Go straight ahead. There is no time to waste.",
                "Examine the drawings on the walls. (Intelligence 4+ required)",
                "Follow the light flow on the floor."
            });

            if (choice == "Go straight ahead. There is no time to waste.")
            {
                ShowText("You decide to move forward without wasting time.");
                ShowText("While you are quickly walking down the corridor, you suddenly trip over a stone and trigger a mechanism!");
                ShowText("Sharp thorns are coming out of the ground and hurting you!");

                player.ApplyDamage(2);
                ShowStats(player);

                if (!player.IsAlive)
                {
                    ShowText("The thorns have caused deep wounds. You are taking your last breath.");
                    return;
                }

                ShowText("You continue on your way, groaning in pain. Finally, you reach the main hall.");
                MainHall();
            }
            else if (choice == "Examine the drawings on the walls. (Intelligence 4+ required)")
            {
                if (player.CheckStat("intelligence", 4))
                {
                    ShowText("You carefully examine the drawings on the walls.");
                    ShowText("The drawings tell the history of the temple: an ancient power was trapped in a crystal object.");
                    ShowText("The ancient shamans tried to control this power but failed and eventually sealed it.");
                    ShowText("Also, the drawings show the locations of the traps in the hallway and how to avoid them!");
                    ShowText("With this knowledge, you safely avoid the traps and reach the main hall.");
                    MainHall();
                }
                else
                {
                    ShowText("You look at the drawings on the walls but you can't make sense of them.");
                    ShowText("You only see some figures and symbols, but they don't make any sense.");
                    ShowText("You continue forward with disappointment and reach the main hall, but you feel like you're missing some important clues.");
                    MainHall();
                }
            }
            else // Follow the light flow on the floor.
            {
                ShowText("You decide to follow the mysterious light stream on the floor.");
                ShowText("The light branches off the main hallway and into a small side room.");
                ShowText("When you enter the room, you see a small altar in the middle with an ancient book on it!");

                Item book = new Item("Ancient Book", "intelligence", 1);
                player.Inventory.AddItem(book);
                player.Inventory.ShowInventory();
                player.AddStat(book.AffectsStat, book.Value);
                ShowItemFound(book);
                ShowStats(player);

                ShowText("You quickly examine the book and learn about the secrets of the temple.");
                ShowText("The book talks about the crystal power source and ways to control it."); 
                ShowText("With this valuable information, you return to the main corridor and proceed to the main hall.");
                MainHall();
            }
        }

        private void VillageDefensePath()
        {
            PressEnterToContinue();
            VillageBoundary();
        }

        private void VillageBoundary()
        {
            ShowText("You build traps and watchtowers around the village under the leadership of Headman Adrian.");
            ShowText("Your work continues until evening. As the sun sets, you see a red glow in the distance as if a fire had been lit.");

            var choice = GetChoice(new[] {
                "Leave your work unfinished and go alone to investigate.",
                "Convince someone else to take your place to continue your work and go alone to investigate. (Charisma 2+ required)"
});

            if (choice == "Leave your work unfinished and go alone to investigate.")
            {
                ShowText("You walk towards the light without telling anyone. People who see you think you're slacking off and are talking behind your back.");
                ShowText("You're losing reputation.");

                player.AddStat("charisma", -1);
                ShowStats(player);

                ShowText("You see a small cabin over the hill.");
                ShowText("The door to the cabin is open and you can hear groaning coming from inside.");
                ShowText("When you enter, you find a wounded stranger on the ground. He's begging for help.");
                StrangerEncounter();
            }
            else // Convince someone else to take your place to continue your work and go alone to investigate. (Charisma 2+ required)
            {
                if (player.CheckStat("charisma", 2))
                {
                    ShowText("You approach someone nearby and ask.");
                    ShowText("\"Can you take over here? I noticed something, I'll be back and report back,\" you say.");
                    ShowText("The man nods: \"Okay, but be careful. It's not very safe around here at night.\"");
                    ShowText("You walk toward the light. You see a small cabin over the hill.");
                    ShowText("The door to the cabin is open and you can hear groaning coming from inside.");
                    ShowText("When you enter, you find a wounded stranger on the ground. He's begging for help.");
                    StrangerEncounter();
                }
                else
                {
                    ShowText("You ask everyone nearby. But no one wants to take over the job.");
                    ShowText("You get tired from wandering around, you waste time, and you feel bad.");

                    player.ApplyDamage(1);
                    ShowStats(player);

                    ShowText("You can't find anyone to delegate your job to, so you walk towards the light alone. You see a small hut behind the hill.");
                    ShowText("The door of the hut is open, and you can hear groaning coming from inside.");
                    ShowText("When you enter, you find a wounded stranger on the ground. He's begging for help.");
                    StrangerEncounter();
                }
            }
        }

        private void StrangerEncounter()
        {
            PressEnterToContinue();
            ShowText("The wounded stranger has come from the Shadow Valley and is badly injured.");
            ShowText("He is wearing strangely symbolized clothes and has deep wounds on his body.");
            ShowText("Before taking his last breath he says: \"T-the temple... darkness... stop him...\"");

            var choice = GetChoice(new[] {
                "Search the stranger's belongings. (Intelligence 2+ required)", 
                "Try to treat the injured."
            });

            if (choice == "Search the stranger's belongings. (Intelligence 2+ required)")
            {
                if (player.CheckStat("intelligence", 2))
                {
                    ShowText("You carefully search the stranger's belongings.");
                    ShowText("A worn parchment comes out of your pocket. When you open it, you notice that it's a map to a temple in the Shadow Valley!");
                    ShowText("A spot is marked on the map and there are some notes.");

                    Item map = new Item("Temple Map", isKeyItem: true);
                    player.Inventory.AddItem(map);
                    player.Inventory.ShowInventory();
                    ShowItemFound(map);

                    ShowText("This map can take you directly to the temple. You immediately go to the Village Headman. You tell him the stranger's last words.");
                    ShowText("The Village Headman shakes his head anxiously: \"These words are worrying. It must be the temple mentioned in the old legends.\"");
                    ShowText("\"Tomorrow morning, we will gather the whole village and discuss this situation.\"");
                    VillageMeeting2();
                }
                else
                {
                    ShowText("You search through the stranger's belongings but can't find anything important.");
                    ShowText("There are only a few loose coins and personal items.");
                    ShowText("You return to the Headman Adrian, disappointed. You tell him the stranger's last words.");
                    ShowText("The headman shakes his head anxiously: \"These words are worrying. It must be the temple mentioned in ancient legends.\"");
                    ShowText("\"Tomorrow morning, we will gather the whole village and discuss this situation.\"");
                    VillageMeeting2();
                }
            }
            else if (choice == "Try to treat the injured.")
            {
                ShowText("You quickly try to heal his wounds, finding clean water and a cloth from around.");
                ShowText("But the stranger's wounds are very deep and seem to be poisoned. No matter what you do, you can't save him.");
                ShowText("You sadly go to the Headman Adrian. You tell him the stranger's last words.");
                ShowText("The headman shakes his head anxiously: \"These words are worrying. It must be the temple mentioned in ancient legends.\"");
                ShowText("\"Tomorrow morning, we will gather the whole village and discuss this situation.\"");
                VillageMeeting2();
            }
        }

        private void VillageMeeting2()
        {
            PressEnterToContinue();
            ShowText("The next morning, all the villagers regroup in the square.");
            ShowText("Headman Adrian: \"It's certain now: something has been unleashed from the ancient temple in the Shadow Village and is threatening our village.\"");
            ShowText("\"According to legend, there was an ancient power in the valley and someone may have unleashed it. Does anyone know about the temple?\"");

            var choices = new string[] {
                "Remain silent and listen to what others have to say."
};

            if (player.Inventory.HasItem("Grandfather's Diary") ||
                player.Inventory.HasItem("Temple Map"))
            {
                choices = new string[] {
                    "Share the information you have.",
                    "Remain silent and listen to what others have to say."
                };
            }

            var choice = GetChoice(choices);

            if (choice == "Share the information you have.")
            {
                ShowText("You come forward and share the information you have.");

                if (player.Inventory.HasItem("Grandfather's Diary"))
                {
                    ShowText("\"My grandfather's journal contains information about the Shadow Valley,\" you say.");
                    ShowText("\"My grandfather mentions that there is an ancient power in the temple in the valley. This power is trapped in a crystal object.\"");
                }

                if (player.Inventory.HasItem("Temple Map"))
                {
                    ShowText("\"And here is the map I found on the stranger. It will take us straight to the temple!\" you say.");
                }

                ShowText("Headman Adrian and the villagers seem impressed.");
                ShowText("\"This information is very valuable,\" says the Headman Adrian. \"You must lead this adventure.\"");
                ShowText("The villagers support you and choose you as the leader for the journey to the Shadow Valley.");

                player.AddStat("charisma", 1);
                ShowStats(player);

                ShadowValleyJourney();
            }
            else if (choice == "Remain silent and listen to what others have to say.")
            {
                ShowText("You stay quiet and wait for the others to speak.");
                ShowText("The village strongman Gareth steps forward: \"I will go to the valley with a group of warriors and eliminate this threat!\"");
                ShowText("Headman Adrian shakes his head: \"Gareth and his men will leave. The others should stay in the village and help defend it.\"");
                ShowText("Gareth looks at you with disdain: \"You prepare to stand guard. We'll do the real work.\"");
                ShowText("The villagers are laughing and you feel left out. However, you don't want to miss out on this adventure and you go to the valley alone.");
                ShadowValleyJourney();
            }
        }

        private void ShadowValleyJourney()
        {
            PressEnterToContinue();
            ShowText("You are heading towards the Shadow Valley.");
            ShowText("The weather is getting darker and the trees around you are getting more eerie.");
            ShowText("You see an ancient temple covered in moss rising in the middle of the valley.");

            var choice = GetChoice(new[] {
                "Go directly to the temple.",
                "Visit the village near the valley and gather information. (Charisma 4+ required)"
});

            if (choice == "Go directly to the temple.")
            {
                ShowText("You don't want to waste time. You are heading directly to the temple.");
                ShowText("Moving fast gives you an advantage, but you are also open to dangers.");
                ShowText("Fortunately, you reach the temple entrance without any serious problems.");
                TempleEntrance2();
            }
            else if (choice == "Visit the village near the valley and gather information. (Charisma 4+ required)")
            {
                if (player.CheckStat("charisma", 4))
                {
                    ShowText("You visit a small village on the edge of the valley.");
                    ShowText("The villagers are skeptical at first, but they trust you because of your friendly approach.");
                    ShowText("An old man pulls you aside: \"If you want to enter the temple, don't use the main gate. There's a secret passage behind it.\"");
                    ShowText("\"The shaman has filled all the entrance paths with traps. The secret passage is on the west side of the temple, behind a large rock.\"");
                    ShowText("With this valuable information, you head towards the temple.");
                    TempleEntrance2(true);
                }
                else
                {
                    ShowText("You reach the village in the valley, but the villagers are afraid of you and close their gates.");
                    ShowText("\"Get out of here! You must be one of the Shaman's men!\" someone shouts.");
                    ShowText("No matter how hard you try, you can't communicate with the villagers. You head towards the temple.");
                    TempleEntrance2();
                }
            }
        }

        private void TempleEntrance2(bool secretPassage = false)
        {
            PressEnterToContinue();
            ShowText("You are standing in front of the temple. Its huge door is covered with mysterious symbols.");

            if (secretPassage)
            {
                ShowText("You remember the secret passage the villager told you about and head towards the west side of the temple.");
                ShowText("There is indeed a small passage behind a large rock!");
                ShowText("Thanks to this passage, you can enter the temple easily and safely.");
                MainHall();
                return;
            }

            ShowText("The door is locked and there is no keyhole on it.");

            var choices = new string[] {
                "Force the door. (Strength 5+ required)",
                "Examine the symbols. (Intelligence 4+ required)",
                "Try to break the door."
            };

            var choice = GetChoice(choices);

            if (choice == "Force the door. (Strength 5+ required)")
            {
                if (player.CheckStat("strength", 5))
                {
                    ShowText("You use your incredible strength to charge the door. The old stones crack and you open the door without breaking it!");
                    ShowText("However, this noise must have alerted the guards inside. You hear an alarm!");
                    ShowText("You rush in.");
                    MainHall(true);
                }
                else
                {
                    ShowText("You try to push the door with all your might without breaking it, but the door is too heavy and sturdy.");
                    ShowText("When you hit the door hard with your shoulder, the small thorns that fly out of the door hurt you!");

                    player.ApplyDamage(2);
                    ShowStats(player);

                    if (!player.IsAlive)
                    {
                        ShowText("The thorns are poisonous! Your body quickly becomes paralyzed and you fall to the ground. No one will come to save you. End of the road.");
                        return;
                    }

                    ShowText("You should try another way.");
                    TempleEntrance2(); // Make the choice again.
                }
            }
            else if (choice == "Examine the symbols. (Intelligence 4+ required)")
            {
                if (player.CheckStat("intelligence", 4))
                {
                    ShowText("You carefully examine the symbols and notice a pattern.");
                    ShowText("You understand that the symbols must be touched in a certain order.");
                    ShowText("When you touch the right symbols, the door opens silently!");
                    ShowText("Thanks to your intelligence, you enter the temple without anyone noticing.");
                    MainHall();
                }
                else
                {
                    ShowText("You look at the symbols but can't make any sense of them.");
                    ShowText("When you touch some random symbols, a poisonous gas erupts from the door!");

                    player.ApplyDamage(2);
                    ShowStats(player);

                    if (!player.IsAlive)
                    {
                        ShowText("The gas is so toxic! You can't breathe and you fall to the ground. No one will come to save you. End of the road");
                        return;
                    }

                    ShowText("You should try another way.");
                    TempleEntrance2(); // Make the choice again.
                }
            }
            else // Try to break the door.
            {
                ShowText("Your strength and intelligence are not enough to open the door, so you start hitting it with a pickaxe you found nearby.");
                ShowText("After a long struggle, you finally break the door, but in the process you activate a trap and an arrow gets stuck in your shoulder!");

                player.ApplyDamage(3);
                ShowStats(player);

                if (!player.IsAlive)
                {
                    ShowText("The injury is very serious! You are losing consciousness due to blood loss. No one will come to save you. End of the road");
                    return;
                }

                ShowText("Despite the pain, you manage to get through the gap. But the noise you made must have alerted those inside!");
                ShowText("You rush in, wounded.");

                MainHall(true);
            }
        }

        private void MainHall(bool alarmTriggered = false)
        {
            PressEnterToContinue();
            ShowText("When you reach the main hall, you see a large stone altar in the middle with a glowing crystal on it.");
            ShowText("The crystal seems to trap the souls of the people who disappeared from your village.");
            ShowText("There is also a shadowy shaman-like figure in the hall.");

            if (alarmTriggered)
            {
                ShowText("The shaman seemed to be waiting for you. \"You've finally arrived\" he says in a threatening voice.");
            }
            else
            {
                ShowText("The shaman appears to be performing a ritual with the crystal, his back to you.");
            }
            var choice = GetChoice(new[] {
                "Attack the shaman. (Strength 4+ required)",
                "Try to talk to the shaman. (Charisma 4+ required)",
                "Buy time to examine the crystal. (Intelligence 4+ required)"
            });
            if (choice == "Attack the shaman. (Strength 4+ required)")
            {
                if (player.CheckStat("strength", 4))
                {
                    ShowText("You rush towards the shaman. You knock him down with a powerful blow!");
                    ShowText("The shaman falls to the ground, dazed. \"Stop... you don't understand...\" he says in a weak voice.");
                    ShowText("But you don't listen. You neutralize the shaman with one move.");
                    ShowText("The shaman is defeated. Now it's time to face the crystal.");
                    FinalConfrontation(true);
                }
                else
                {
                    ShowText("You attack the shaman, but he's too fast! He takes a step back and dark energy gushes from his hands.");
                    ShowText("The energy hits you and you fall to the ground in pain!");

                    player.ApplyDamage(4);
                    ShowStats(player);

                    if (!player.IsAlive)
                    {
                        GameOver(GameEndingType.Failure);
                        return;
                    }

                    ShowText("Despite the pain, you stand up. The shaman continues to control the crystal.");
                    FinalConfrontation(false);
                }
            }
            else if (choice == "Try to talk to the shaman. (Charisma 4+ required)")
            {
                if (player.CheckStat("charisma", 4))
                {
                    ShowText("You slowly approach the shaman and speak in a calm voice: \"I have not come to harm you. What are you trying to do?\"");
                    ShowText("When the shaman turns to face you, you are met with a familiar face. It is Morathor, the old sage who was kidnapped from your village!");
                    ShowText("\"Do you recognize me?\" he asks, a struggle in his eyes. \"They... are controlling me... the crystal...\"");
                    ShowText("Morathor tells you that a powerful ancient being has taken over him and is using the crystal to collect more souls.");
                    ShowText("\"Help me... we must destroy the crystal or control it...\" he says in a shaky voice.");
                    FinalConfrontation(true, true);
                }
                else
                {
                    ShowText("You try to talk to the shaman, but your words don't seem to reach him.");
                    ShowText("The shaman suddenly turns in anger and shoots purple light from his hands!");
                    ShowText("The beam hits you, and you feel a searing pain in your chest.");

                    player.ApplyDamage(2);
                    ShowStats(player);

                    if (!player.IsAlive)
                    {
                        GameOver(GameEndingType.Failure);
                        return;
                    }

                    ShowText("You stumble, but you stay standing. What do you do now?");
                    FinalConfrontation(false);
                }
            }
            else // Buy time to examine the crystal. (Intelligence 4+ required)
            {
                if (player.CheckStat("intelligence", 4))
                {
                    ShowText("You ask questions to distract the shaman while you examine the crystal.");
                    ShowText("You recognize the ancient symbols on the crystal's surface! It's an ancient talisman of protection.");
                    ShowText("You discover the crystal's weak point and how to control it.");
                    ShowText("This information will give you an advantage when you confront the crystal.");
                    FinalConfrontation(false, false, true);
                }
                else
                {
                    ShowText("As you try to examine the crystal, you feel a strange pull. You can't take your eyes off it.");
                    ShowText("Suddenly, the crystal glows and a sharp pain shoots through your mind!");

                    player.ApplyDamage(2);
                    ShowStats(player);

                    if (!player.IsAlive)
                    {
                        GameOver(GameEndingType.Failure);
                        return;
                    }

                    ShowText("You are pulled back from the crystal, dizzy but conscious.");
                    FinalConfrontation(false);
                }
            }
        }

        private void FinalConfrontation(bool shamanDefeated, bool shamanConvinced = false, bool crystalKnowledge = false)
        {
            PressEnterToContinue();
            ShowText("THE MOMENT OF DECISION");

            if (shamanDefeated)
            {
                ShowText("As the defeated shaman lies on the ground, the crystal begins to burn brighter.");
            }
            else if (shamanConvinced)
            {
                ShowText("The crystal vibrates dangerously as Morathor fights himself.");
                ShowText("\"Choose... quickly... the crystal... is unstable...\" he says breathlessly.");
            }
            else
            {
                ShowText("The shaman and the crystal stand before you. A true test of strength awaits you.");
            }

            ShowText("The spirits in the crystal are calling out to you as if they are waiting to be saved.");

            // Bonusları açıkla
            string destroyOption = "Destroy the crystal. (Strength 5+ required)";
            string controlOption = "Try to control the crystal. (Intelligence 5+ required)";
            string convinceOption = "Convince the shaman to release the crystal. (Charisma 5+ required)";

            if (shamanDefeated)
            {
                destroyOption += " [Shaman defeated: +1 bonus]";
            }
            if (shamanConvinced)
            {
                convinceOption += " [Morathor helps: +1 bonus]";
            }
            if (crystalKnowledge)
            {
                controlOption += " [Crystal knowledge: +1 bonus]";
            }

            var choice = GetChoice(new[] { destroyOption, controlOption, convinceOption });

            if (choice.StartsWith("Destroy the crystal."))
            {
                int bonus = shamanDefeated ? 1 : 0;
                if (player.CheckStat("strength", 5 - bonus))
                {
                    ShowText("You gather all your strength and lunge for the crystal!");
                    ShowText("When your fist hits the crystal, there is a deafening crack.");
                    ShowText("The crystal cracks, shatters, and there is a burst of light.");
                    ShowText("Meanwhile, the crystal also harms you.");
                    ShowText("When you open your eyes, the crystal has vanished and the villagers who were lost in the hall have begun to appear!");

                    player.ApplyDamage(1);
                    ShowStats(player);

                    if (player.Health <= 0)
                    {
                        GameOver(GameEndingType.Sacrifice);
                    }
                    else
                    {
                        GameOver(GameEndingType.Hero);
                    }
                }
                else
                {
                    ShowText("You try to break the crystal, but it's too strong! When your fist hits it, it vibrates dangerously!");
                    ShowText("Suddenly, the crystal glows and a wave of energy knocks you back!");

                    player.ApplyDamage(3);
                    ShowStats(player);

                    if (!player.IsAlive)
                    {
                        GameOver(GameEndingType.Failure);
                    }
                    else
                    {
                        ShowText("You get up in pain. The crystal is still there, but it seems more unstable.");
                        LastChance();
                    }
                }
            }
            else if (choice.StartsWith("Try to control the crystal."))
            {
                int bonus = crystalKnowledge ? 1 : 0;
                if (player.CheckStat("intelligence", 5 - bonus))
                {
                    ShowText("You reach out your hands to the crystal and open your mind.");
                    ShowText("At first you resist, but then you manage to connect. You communicate with the spirits within the crystal.");
                    ShowText("You gradually gain control and the crystal begins to move at your will.");
                    ShowText("With the power of the crystal, you release the trapped spirits and the crystal emits a calm blue light as it fades.");

                    GameOver(GameEndingType.Hero);
                }
                else
                {
                    ShowText("You try to control the crystal, but its power is too great! Your mind is drowning.");
                    ShowText("Suddenly, you feel a shift in your mind... The power of the crystal takes over you!");
                    ShowText("You feel a dark power rising inside you...");

                    GameOver(GameEndingType.Tragic);
                }
            }
            else // Convince the shaman to release the crystal.
            {
                int bonus = shamanConvinced ? 1 : 0;
                if (player.CheckStat("charisma", 5 - bonus))
                {
                    if (shamanConvinced)
                    {
                        ShowText("You tell Morathor that he must release the crystal for the good of the village and everyone else.");
                        ShowText("Morathor falls to his knees. There is a struggle in his eyes. He struggles with the entity that has taken over his body");
                        ShowText("As he raises his trembling hands to the crystal, he mouths the words: \"This is enough...\"");
                    }
                    else
                    {
                        ShowText("You explain to the shaman that this power will destroy him and harm everyone.");
                        ShowText("The darkness in his eyes slowly fades. He falls to his knees. There seems to be a struggle in his eyes.");
                        ShowText("As he raises his trembling hands to the crystal, he mouths the words: \"This is enough...\"");
                    }

                    ShowText("The shaman loosens his grip on the crystal and it slowly begins to fade.");
                    ShowText("The spirits within the crystal are released and the shaman is freed.");

                    GameOver(GameEndingType.Hero);
                }
                else
                {
                    ShowText("You try to convince the shaman, but your words don't reach him.");
                    ShowText("\"Too late!\" he shouts and sends a wave of dark energy towards you!");

                    player.ApplyDamage(3);
                    ShowStats(player);

                    if (!player.IsAlive)
                    {
                        GameOver(GameEndingType.Failure);
                    }
                    else
                    {
                        ShowText("The attack knocks you down, but you still have the strength to fight.");
                        LastChance();
                    }
                }
            }
        }

        private void LastChance()
        {
            PressEnterToContinue();
            ShowText("You have one last chance. What will you do?");

            var choice = GetChoice(new[] {
                "Try to break the crystal with your last strength.",
                "Try to talk to the shaman one last time. (Charisma 3+ required)",
                "Retreat and return to the village."
            });

            if (choice == "Try to break the crystal with your last strength.")
            {
                ShowText("You gather your last remaining strength and lunge towards the crystal.");
                ShowText("This time, your determination is stronger! When your fist hits the crystal, it shatters!");
                ShowText("There's a powerful burst of light, and then... silence.");

                player.ApplyDamage(1);
                ShowStats(player);

                if (!player.IsAlive)
                {
                    GameOver(GameEndingType.Sacrifice);
                }
                else
                {
                    GameOver(GameEndingType.Hero);
                }
            }
            else if (choice == "Try to talk to the shaman one last time. (Charisma 3+ required)")
            {
                ShowText("In a last-ditch effort, you call out to the shaman: \"Think of what's best for everyone. This power will destroy you!\"");

                if (player.CheckStat("charisma", 3))
                {
                    ShowText("Surprisingly, your words affect the shaman. The darkness in his eyes is diminishing.");
                    ShowText("The crystal is slowly fading and the spirits are being released.");

                    GameOver(GameEndingType.Hero);
                }
                else
                {
                    ShowText("The shaman laughs: \"It's too late!\" and a final wave of energy from the crystal hits you.");
                    player.ApplyDamage(10);

                    GameOver(GameEndingType.Failure);
                }
            }
            else // Retreat and return to the village.
            {
                ShowText("You decide to retreat. You realize you can't win this war.");
                ShowText("You manage to escape from the temple, but you know the danger you leave behind.");
                ShowText("When you return to the village, you know that the dark power in the temple will eventually spread.");

                GameOver(GameEndingType.Failure);
            }
        }

        private enum GameEndingType
        {
            Hero,          // Best ending: You saved the village.
            Tragic,        // Bad ending: The crystal took you over.
            Sacrifice,     // Mixed ending: You saved the village but you did not survive.
            Failure        // Worst ending: You failed.
        }

        private void GameOver(GameEndingType endingType)
        {
            PressEnterToContinue();
            ShowText("========== OYUN SONU ==========");

            switch (endingType)
            {
                case GameEndingType.Hero:
                    ShowText("HERO ENDING");
                    ShowText("You successfully controlled the crystal and saved everyone who was lost in the village!");
                    ShowText("You return to Riverwood as a hero. The villagers welcome you with joy");
                    ShowText("Your story will be told from generation to generation.");
                    break;

                case GameEndingType.Tragic:
                    ShowText("TRAGIC ENDING");
                    ShowText("The power of the crystal has taken over your mind! You are no longer you.");
                    ShowText("When you return to the village, there is a dark gleam in your eyes.");
                    ShowText("You are the new threat to Riverwood.");
                    ShowText("And Riverwood village is just the beginning.");
                    break;

                case GameEndingType.Sacrifice:
                    ShowText("END OF SACRIFICE");
                    ShowText("You have managed to destroy the crystal by sacrificing your own life.");
                    ShowText("You are known as a hero in the village and your story will be told from generation to generation.");
                    ShowText("Your sacrifice will never be forgotten.");
                    break;

                case GameEndingType.Failure:
                    ShowText("END OF FAILURE");
                    ShowText("You have failed in your quest.");
                    ShowText("The dark power in the temple in Shadow Valley continues to grow uncontrollably.");
                    ShowText("Riverwood village is still in great danger.");
                    ShowText("And Riverwood village is just the beginning.");
                    break;
            }

            ShowText("\nYour Final Status:");
            ShowStats(player);
            ShowText("\nThank you for playing!");
            PressEnterToContinue();
            Environment.Exit(0);
        }
    }
}