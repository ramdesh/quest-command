# Quest Command

An open world rpg with intense levels of interact, experienced through the command prompt

## Design

### Goals

Create a world more interactive than Skyrim by trading presentation layer for higher levels of interaction.

### Design Pillars

* Architecture is flat and increases in complexity relative to project size in as small of increments as possible.
* Classes are small, decoupled, and do just one thing
* Dependency and coupling are kept to a minimum

### Design Principles

* Commands simply parse / understand user input and then create events
* Commands do not handle or change state
* All intents and actions are created through events
* Listeners subscribe to individual events, update gamestate and print to console.

### Design Notes

Any time an activator adds a new triggered event, it needs to be added to the triggered event when statement

Location positions are always relative to their parent. The parent is always (0,0,0). If a location is compared with a location outside the parent, the parent locations are compared.


## Planning and Ideas

### Research
* manic mansion SCUM
* ducktype
* context free gramer, tokenizers, lexers, (yacc, lex)


### Command ideas
* Search - skill based, finds scope that's hidden
* Look (examine) object for its description
* Battle Commands (each can take a direction), (default to item in right hand, option to add left)
  * Slash 
  * Chop
  * Stab
  * Dodge
  * Block (high, low, medium)
  * Step (forward, back) (ranges: knife, sword, lance, bow)
  
  **Battle**
  
 In battle, a creature can act when it attains 100 action points. Action points are increased by the creature's agility, alternating between the two creatures. When 100 ap are achieved, the creature gets a turn and the ap are reset to 0. During a turn, a player can take any normal command / action, but will generally choose a battle action. The player can equip either hand, or use a hand. Each of the battle commands default's to the right hand, but can be preferenced with right or left to indicate a hand. If block is chosen without a hand preference, it will default to the hand that has a shield, is armed, or the right hand. 
 
 Block can be high, low or medium. If the player blocks high and the opponent strikes high, the shield will take all damagage. If the opponent strikes medium (one height away from block area), the shield takes medium damage (and low, the sheild would block no damage).
 
 Weapons have different reach and the player may need to step closer, or step back to avoid a blow.

## TODO
* find a way to allow auto complete with tab
* Story manager listens to all events, compares plot point criteria to each event to see if plot point should run
* Skill check for climb
* Better use command debug messages, refactor big if statement
  * If target not found, give error message, not just explain item
* Flamable Objects should be able to be set on fire
  * Open field (grass)
  * Apple Tree
* Tree should be scratched by dagger, cut down by axe
  * Cut down should turn into logs
* Apple should be roasted, roasted apple should be burnt
* Items should be equippable
* Inventory carrying space
* Better package organization for actions
* Jump down from location (goes to parent location)
* Packages are still a mess
* Give json inherit keywords. Spawning grabs all properties etc from inherited
* Arrive event include a method of travel?

**Validation tools**
* Seperate from test suite
* Check for duplicate names (across items and activators)
* Check for duplicate command aliases
* Check all targets reference valid events etc (triggers and trigger events)




  


