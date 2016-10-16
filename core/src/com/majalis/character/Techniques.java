package com.majalis.character;

import com.badlogic.gdx.utils.Array;
import com.majalis.character.AbstractCharacter.Stance;
import com.majalis.technique.AttackTechnique;
import com.majalis.technique.ClimaxTechnique;
import com.majalis.technique.ClimaxType;
import com.majalis.technique.FallDownTechnique;
import com.majalis.technique.GrappleTechnique;
import com.majalis.technique.GuardTechnique;
import com.majalis.technique.NonAttackTechnique;
import com.majalis.technique.SpellTechnique;
import com.majalis.technique.TechniquePrototype;
import com.majalis.technique.TechniquePrototype.TechniqueHeight;
/*
 * List of all techniques and their generic attributes.
 */
public enum Techniques {
	/* Offensive Techniques */  
	POWER_ATTACK 		(new AttackTechnique(Stance.OFFENSIVE, Stance.OFFENSIVE, "Power Attack", 3, 3, 4)),
	TEMPO_ATTACK  		(new AttackTechnique(Stance.OFFENSIVE, Stance.OFFENSIVE, "Tempo Attack", 2, 2, 3)),
	SPRING_ATTACK  		(new AttackTechnique(Stance.BALANCED, Stance.OFFENSIVE, "Spring Attack", 1, 4, 3)),
	
	/* Blitz Techniques */
	BLITZ_ATTACK  		(new AttackTechnique(Stance.OFFENSIVE, Stance.BLITZ, "Blitz Attack", 4, 4, 6)),
	ALL_OUT_BLITZ		(new AttackTechnique(Stance.BLITZ, Stance.BLITZ, "All-Out Blitz", 6, 4, 8, 1.5)),
	HOLD_BACK			(new NonAttackTechnique(Stance.BLITZ, Stance.OFFENSIVE, "Hold Back", 0, 3)),
	
	/* Balanced Techniques */
	RESERVED_ATTACK  	(new AttackTechnique(Stance.OFFENSIVE, Stance.BALANCED, "Reserved Attack", 1, 4, 1)),
	REVERSAL_ATTACK  	(new AttackTechnique(Stance.DEFENSIVE, Stance.BALANCED, "Reversal Attack", 0, 2, 2)),
	NEUTRAL_ATTACK  	(new AttackTechnique(Stance.BALANCED, Stance.BALANCED, "Neutral Attack", 0, 1, 1)),
	/* Defensive Techniques */
	CAREFUL_ATTACK  	(new AttackTechnique(Stance.DEFENSIVE, Stance.DEFENSIVE, "Careful Attack", 0, 0, 1)),
	BLOCK				(new GuardTechnique	(Stance.BALANCED, Stance.DEFENSIVE, "Block", 0, 0)),
	GUARD  				(new GuardTechnique	(Stance.DEFENSIVE, Stance.DEFENSIVE, "Guard", -2, -2)),
	SECOND_WIND			(new NonAttackTechnique(Stance.DEFENSIVE, Stance.DEFENSIVE, "Second Wind", -4, -1)),
	/* Techniques from Prone/Supine */
	KIP_UP				(new NonAttackTechnique(Stance.PRONE, Stance.BALANCED, "Kip Up", 5, -10)),
	STAND_UP			(new NonAttackTechnique(Stance.PRONE, Stance.BALANCED, "Stand Up", 3, -6)),
	KNEE_UP				(new NonAttackTechnique(Stance.PRONE, Stance.KNEELING, "Knee Up", 1, -10)),
	REST				(new NonAttackTechnique(Stance.SUPINE, Stance.SUPINE, "Rest", -1, -1)),
	REST_FACE_DOWN		(new NonAttackTechnique(Stance.PRONE, Stance.PRONE, "Rest", -1, -1)),
	/* Out of resources */
	FIZZLE				(new NonAttackTechnique(Stance.CASTING, Stance.BALANCED, "Fizzle", 0, 0)),	 // run out of mana
	/* Positional */
	DUCK				(new NonAttackTechnique(Stance.BALANCED, Stance.KNEELING, "Duck", 0, 0)),	 
	FLY					(new NonAttackTechnique(Stance.BALANCED, Stance.AIRBORNE, "Fly", 0, 0)),	 
	/* Enemy attacks */
	SLIME_ATTACK 		(new AttackTechnique(Stance.BALANCED, Stance.BALANCED, "Slime Attack", 7, 0, 5)),
	SLIME_QUIVER 		(new NonAttackTechnique(Stance.BALANCED, Stance.DEFENSIVE, "Slime Quiver", -1, -1)),
	GUT_CHECK			(new AttackTechnique(Stance.OFFENSIVE, Stance.OFFENSIVE, "Gutcheck", 3, 3, 4, 0, 1, false, TechniqueHeight.MEDIUM)),
	
	/* Enemy pouncing */
	DIVEBOMB 			(new GrappleTechnique  (Stance.AIRBORNE, Stance.FELLATIO, "Divebomb", 2, Stance.FELLATIO, TechniqueHeight.HIGH)),
	SAY_AHH 			(new GrappleTechnique  (Stance.BALANCED, Stance.FELLATIO, "Say 'Ahh'", 2, Stance.FELLATIO)),
	IRRUMATIO 			(new NonAttackTechnique(Stance.FELLATIO, Stance.FELLATIO, "Irrumatio", 0, 0)), 
	POUNCE_DOGGY		(new GrappleTechnique  (Stance.BALANCED, Stance.DOGGY, "Pounce", 2, Stance.DOGGY)), // Used to initiate doggy
	POUND_DOGGY 		(new NonAttackTechnique(Stance.DOGGY, Stance.DOGGY, "Pound", 0, 1)), // Used to doggystyle
	POUNCE_ANAL			(new GrappleTechnique  (Stance.BALANCED, Stance.ANAL, "Pounce", 2, Stance.ANAL)), // Used to initiate missionary
	POUND_ANAL 			(new NonAttackTechnique(Stance.ANAL, Stance.ANAL, "Pound", 0, 1)), // Used to missionary
	KNOT 				(new NonAttackTechnique(Stance.DOGGY, Stance.KNOTTED, "Knot", 0, 0, Stance.KNOTTED, "Set Damage")), // Used to knot by knotty weresluts and others
	KNOT_BANG 			(new NonAttackTechnique(Stance.KNOTTED, Stance.KNOTTED, "Knot Bang", 0, 0, Stance.KNOTTED, true)), // Used to knot by knotty weresluts and others - could end the battle
	ERUPT_ANAL 			(new ClimaxTechnique   (Stance.DOGGY, Stance.DOGGY, "Erupt", Stance.PRONE, ClimaxType.ANAL )),
	ERUPT_ORAL 			(new ClimaxTechnique   (Stance.FELLATIO, Stance.FELLATIO, "Erupt", Stance.KNEELING, ClimaxType.ORAL )),
	
	RECEIVE_DOGGY		(new NonAttackTechnique(Stance.DOGGY, Stance.DOGGY, "Receive", 0, 0)), 
	RECEIVE_ANAL		(new NonAttackTechnique(Stance.ANAL, Stance.ANAL, "Receive", 0, 0)), 
	RECEIVE_KNOT 		(new NonAttackTechnique(Stance.KNOTTED, Stance.KNOTTED, "Receive Knot", 0, 0)), 
	OPEN_WIDE 			(new NonAttackTechnique(Stance.FELLATIO, Stance.FELLATIO, "Open Wide", 0, 0)), 
	STRUGGLE_DOGGY		(new GrappleTechnique(Stance.DOGGY, Stance.DOGGY, "Struggle", 4)),
	STRUGGLE_ANAL		(new GrappleTechnique(Stance.ANAL, Stance.ANAL, "Struggle", 4)),
	STRUGGLE_ORAL		(new GrappleTechnique(Stance.FELLATIO, Stance.FELLATIO, "Struggle", 4)), 
	BREAK_FREE_ANAL		(new NonAttackTechnique(Stance.ANAL, Stance.BALANCED, "Struggle", 0, 0, Stance.BALANCED)), // Break hold
	BREAK_FREE_ORAL		(new NonAttackTechnique(Stance.FELLATIO, Stance.BALANCED, "Struggle", 0, 0, Stance.BALANCED)), // Break hold
	
	INCANTATION 		(new NonAttackTechnique(Stance.BALANCED, Stance.CASTING, "Incantation", 0, 1)), 
	
	/* Learnable Skills*/
	ARMOR_SUNDER		(new AttackTechnique(Stance.OFFENSIVE, Stance.OFFENSIVE, "Armor Sunder", 1, 7, 4, 0, 2, 0, true, TechniqueHeight.MEDIUM), 3),
	CAUTIOUS_ATTACK  	(new AttackTechnique(Stance.BALANCED, Stance.DEFENSIVE, "Cautious Attack", -1, 0, 2), 3),
	VAULT 				(new NonAttackTechnique(Stance.OFFENSIVE, Stance.AIRBORNE, "Vault", 2, 4)), 
	JUMP_ATTACK 		(new AttackTechnique(Stance.AIRBORNE, Stance.BALANCED, "Jump Attack", 4, 4, 2)),
	RECKLESS_ATTACK 	(new AttackTechnique(Stance.OFFENSIVE, Stance.OFFENSIVE, "Reckless Attack", 2, 3, 6, false), 3), // unguardable
	
	TAUNT 				(new NonAttackTechnique(Stance.DEFENSIVE, Stance.DEFENSIVE, "Taunt", 0, 0, true), 2), 
	KNOCK_DOWN 			(new AttackTechnique(Stance.OFFENSIVE, Stance.OFFENSIVE, "Knock Down", 1, 3, 6, 2), 3), 
	HIT_THE_DECK		(new FallDownTechnique(Stance.BALANCED, Stance.PRONE, "Hit the Deck")), 
	PARRY  				(new GuardTechnique(Stance.DEFENSIVE, Stance.DEFENSIVE, "Parry", -1, 0)),
	
	COMBAT_HEAL  		(new SpellTechnique(Stance.CASTING, Stance.BALANCED, "Combat Heal", 10, 10, true), 3),
	COMBAT_FIRE  		(new SpellTechnique(Stance.CASTING, Stance.BALANCED, "Combat Fire", 3, 5, false), 3),
	TITAN_STRENGTH  	(new SpellTechnique(Stance.CASTING, Stance.BALANCED, "Titan Strength", 0, 2, false), 3),
	
	;
	
	private final TechniquePrototype trait;
	private final int maxRank;
	private Techniques(TechniquePrototype trait){
		this(trait, 1);
	}
	private Techniques(TechniquePrototype trait, int maxRank){
		this.trait = trait;
		this.maxRank = maxRank;
	}
	
	public TechniquePrototype getTrait(){ return trait; }
	
	public int getMaxRank() { return maxRank; }
	
	public static Array<Techniques> getLearnableSkills() {
		Techniques[] learnables = new Techniques[]{ARMOR_SUNDER, CAUTIOUS_ATTACK, VAULT, RECKLESS_ATTACK, TAUNT, KNOCK_DOWN, SECOND_WIND, HIT_THE_DECK, PARRY};
		return new Array<Techniques>(true, learnables, 0, learnables.length);
	}
	public static Array<Techniques> getLearnableSpells() {
		// need to change this to actually include Titan Strength once it's implemented
		return new Array<Techniques>(true, new Techniques[]{COMBAT_HEAL, COMBAT_FIRE, TITAN_STRENGTH}, 0, 2);
	}
}