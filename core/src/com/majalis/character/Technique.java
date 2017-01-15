package com.majalis.character;

import java.util.Comparator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.majalis.character.AbstractCharacter.Stance;
import com.majalis.character.AbstractCharacter.Stat;
import com.majalis.character.Attack.Status;
import com.majalis.technique.TechniquePrototype;
import com.majalis.technique.Bonus;
import com.majalis.technique.Bonus.BonusCondition;
import com.majalis.technique.Bonus.BonusType;
import com.majalis.technique.TechniquePrototype.TechniqueHeight;
/*
 * Represents an action taken by a character in battle.  Will likely need a builder helper.
 */
public class Technique {
	private final TechniquePayload initialPayload;
	private final TechniquePrototype technique;
	private final CharacterState currentState;
	private final int skillLevel;
	private TechniquePayload cachedPayload;
	
	private class TechniquePayload {
		private final TechniquePrototype technique;
		private final Array<Bonus> bonuses;
		// after bonuses
		private final int basePower;
		private final int powerMod;
		private final int staminaCost;
		private final int stabilityCost;
		private final int manaCost;
		private final int block;
		private final int armorSunder;
		private final int gutCheck;
		private final double knockdown;
		private final boolean hasPriority;
		
		//before bonuses
		//private final int powerModBeforeBonuses;
		
		private TechniquePayload(TechniquePrototype technique, CharacterState currentState, int skillLevel, Array<Bonus> toApply) {
			this.technique = technique;
			this.bonuses = toApply;
			this.basePower = technique.isSpell() ? (technique.getBuff() != null ? currentState.getRawStat(Stat.MAGIC) : currentState.getStat(Stat.MAGIC)): technique.isTaunt() ? currentState.getRawStat(Stat.CHARISMA) : 
				// should check if technique can use weapon, and the weapon base damage should come from currentState.getWeaponDamage() rather than exposing these weapons
				currentState.getStat(Stat.STRENGTH) + (currentState.getWeapon() != null ? currentState.getWeapon().getDamage(currentState.getStats()) : 0);	
			int powerCalc = technique.getPowerMod();
			//powerModBeforeBonuses = powerCalc;
			int staminaCalc = technique.getStaminaCost();
			int stabilityCalc = technique.getStabilityCost();
			int manaCalc = technique.getManaCost();
			// this should also include + currentState.getGuardMod
			int blockCalc = technique.getGuardMod();
			int armorSunderCalc = technique.getArmorSunder();
			int gutCheckCalc = technique.getGutCheck();
			double knockdownCalc = technique.getKnockdown();
			boolean hasPriorityCalc = false;
			
			for (Bonus bonusBundle : toApply) {	
				for (ObjectMap.Entry<BonusType, Integer> bonus : bonusBundle.getBonusMap()) {
					switch(bonus.key) {
						case ARMOR_SUNDER:
							armorSunderCalc += bonus.value;
							break;
						case GUARD_MOD:
							blockCalc  += bonus.value;
							break;
						case GUT_CHECK:
							gutCheckCalc += bonus.value;
							break;
						case KNOCKDOWN:
							knockdownCalc += bonus.value;
							break;
						case MANA_COST:
							manaCalc += bonus.value;
							break;
						case POWER_MOD:
							powerCalc += bonus.value;
							break;
						case STABILTIY_COST:
							stabilityCalc += bonus.value;
							break;
						case STAMINA_COST:
							staminaCalc += bonus.value;
							break;
						case PRIORITY:
							hasPriorityCalc = true;
							break;
					}
				}
			}
			
			powerMod = powerCalc;
			block = blockCalc;
			staminaCost = staminaCalc;
			stabilityCost = stabilityCalc;
			manaCost = manaCalc;
			armorSunder = armorSunderCalc;
			gutCheck = gutCheckCalc;
			knockdown = knockdownCalc;
			hasPriority = hasPriorityCalc;
		}
		
		private int getStaminaCost() {
			return staminaCost;
		}
		private int getStabilityCost() {
			return stabilityCost;
		}
		private int getManaCost() {
			return manaCost;
		}
		private int getBlock() {
			return block;
		}
		
		private int getTotalPower() {
			return basePower + powerMod;
		}
		
		private int getDamage() {
			int damage = technique.doesSetDamage() ? 4 : technique.isDamaging() && technique.getGutCheck() == 0 ? getTotalPower() : 0;
			if (damage < 0) damage = 0;
			return damage;
		}
		
		private double getKnockdown() {
			return knockdown;
		}
		
		private int getArmorSunder() {
			return armorSunder;
		}
		
		private int getGutCheck() {
			return gutCheck;
		}
		
		private boolean hasPriority() {
			return hasPriority;
		}
		
		private Array<Bonus> getBonuses() {
			return bonuses;
		}
		
	}
	
	public Technique(TechniquePrototype technique, CharacterState currentState, int skillLevel) {
		initialPayload = applyBonuses(technique, currentState, skillLevel);
		this.technique = technique;
		this.currentState = currentState;
		this.skillLevel = skillLevel;
	}
	
	private TechniquePayload applyBonuses(TechniquePrototype technique, CharacterState currentState, int skillLevel) {
		Array<Bonus> bonusesToApply = new Array<Bonus>();
		for ( ObjectMap.Entry<BonusCondition, Bonus> bonusToCheck: technique.getBonuses().entries()) {
			int bonusLevel = doesBonusApply(technique, currentState, skillLevel, bonusToCheck.key);
			if (bonusLevel > 0) {
				bonusesToApply.add(bonusToCheck.value.combine(bonusLevel));
			}
		}
		return new TechniquePayload(technique, currentState, skillLevel, bonusesToApply);
	}
	
	private int doesBonusApply(TechniquePrototype technique, CharacterState currentState, int skillLevel, BonusCondition toCheck) {
		switch(toCheck) {
			case ENEMY_LOW_STABILITY:
				return currentState.getEnemyLowStability() ? 1 : 0;
			case ENEMY_ON_GROUND:
				return currentState.isEnemyOnGround() ? 1 : 0;
			case SKILL_LEVEL:
				return skillLevel;
			case OUTMANEUVER:
			case OUTMANUEVER_STRONG:
			case STRENGTH_OVERPOWER:
			case STRENGTH_OVERPOWER_STRONG:
			case ENEMY_BLOODY:
			default: return 0;
		}
	}
	
	private TechniquePayload applyBonuses(TechniquePrototype technique, CharacterState currentState, int skillLevel, Technique otherTechnique) {
		Array<Bonus> bonusesToApply = new Array<Bonus>();
		for ( ObjectMap.Entry<BonusCondition, Bonus> bonusToCheck: technique.getBonuses().entries()) {
			int bonusLevel = doesBonusApply(technique, currentState, skillLevel, bonusToCheck.key, otherTechnique);
			if (bonusLevel > 0) {
				bonusesToApply.add(bonusToCheck.value.combine(bonusLevel));
			}
		}
		return new TechniquePayload(technique, currentState, skillLevel, bonusesToApply);
	}
	
	private int doesBonusApply(TechniquePrototype technique, CharacterState currentState, int skillLevel, BonusCondition toCheck, Technique otherTechnique) {
		switch(toCheck) {
			case ENEMY_BLOODY:
				return 0;
			case ENEMY_LOW_STABILITY:
				return currentState.getEnemyLowStability() ? 1 : 0;
			case ENEMY_ON_GROUND:
				return currentState.isEnemyOnGround() ? 1 : 0;
			case SKILL_LEVEL:
				return skillLevel;
			case OUTMANEUVER:
				return currentState.getStat(Stat.AGILITY) - otherTechnique.getStat(Stat.AGILITY);
			case OUTMANUEVER_STRONG:
				return (currentState.getStat(Stat.AGILITY) - otherTechnique.getStat(Stat.AGILITY)) - 3;
			case STRENGTH_OVERPOWER:
				return currentState.getStat(Stat.STRENGTH) - otherTechnique.getStat(Stat.STRENGTH);
			case STRENGTH_OVERPOWER_STRONG:
				return (currentState.getStat(Stat.STRENGTH) - otherTechnique.getStat(Stat.STRENGTH)) - 3;
			default: return 0;
		}
	}
	
	private int getStat(Stat stat) {
		return currentState.getStat(stat);
	}

	// when a technique to interact with this one is established, this generates the final technique, which is used to extract 
	private TechniquePayload getPayload(Technique otherTechnique) {
		if (cachedPayload != null) return cachedPayload;
		TechniquePayload payload = applyBonuses(technique, currentState, skillLevel, otherTechnique);
		cachedPayload = payload;
		return payload;
	}
	// returns the resulting Attack generated by this Technique, having passed through an opposing technique
	public Attack resolve(Technique otherTechnique) {
		
		TechniquePayload otherPayload = otherTechnique.getPayload(this);
		TechniquePayload thisPayload = getPayload(otherTechnique);
		
		int rand = (int) Math.floor(Math.random() * 100);
		double blockMod = technique.isBlockable() ? (otherPayload.getBlock() > rand * 2 ? 0 : otherPayload.getBlock() > rand ? .5 : 1) : 1;
		
		boolean isSuccessful = 
				technique.getTechniqueHeight() == TechniqueHeight.NONE ||
				(technique.getTechniqueHeight() == TechniqueHeight.HIGH && otherTechnique.getStance().receivesHighAttacks) || 
				(technique.getTechniqueHeight() == TechniqueHeight.MEDIUM && otherTechnique.getStance().receivesMediumAttacks) || 
				(technique.getTechniqueHeight() == TechniqueHeight.LOW && otherTechnique.getStance().receivesLowAttacks) 
				;
		// this is temporarily to prevent struggling from failing to work properly on the same term an eruption or knot happens
		boolean failure = false;
		if (isSuccessful) {
			isSuccessful = otherTechnique.getForceStance() == null || otherTechnique.getForceStance() == Stance.KNOTTED || otherTechnique.getForceStance() == Stance.KNEELING || (thisPayload.hasPriority() && !otherPayload.hasPriority());
			failure = !isSuccessful;
		}
		boolean fizzle = thisPayload.getManaCost() > currentState.getMana();
		
		return new Attack(
			fizzle ? Status.FIZZLE : isSuccessful ? Status.SUCCESS : failure ? Status.FAILURE : Status.MISS, 
			technique.getName(), 
			(int)(thisPayload.getDamage() * blockMod), 
			((int) ((thisPayload.getTotalPower()) * thisPayload.getKnockdown()))/2, 
			((int)(thisPayload.getDamage() * blockMod) * thisPayload.getArmorSunder() ) /4, 
			thisPayload.getTotalPower() * thisPayload.getGutCheck(), 
			technique.isHealing() ? thisPayload.getTotalPower() : 0,
			technique.isTaunt() ? thisPayload.getTotalPower() : 0, 
			technique.isGrapple() ? thisPayload.getTotalPower() : 0,
			technique.getClimaxType(), 
			getForceStance(),
			technique.isSpell(),
			new Buff(technique.getBuff(), thisPayload.getTotalPower()),
			technique.isDamaging() && !technique.doesSetDamage(),
			thisPayload.getBonuses()
		);
	}

	public Stance getStance() {
		return technique.getResultingStance();
	}

	protected int getStaminaCost() {
		// should be payload.getStaminaCost
		return initialPayload.getStaminaCost();
	}
	
	protected int getStabilityCost() {
		// should be payload.getStabiityCost
		return initialPayload.getStabilityCost();
	}
	
	protected int getManaCost() {
		return initialPayload.getManaCost();
	}
	
	private Stance getForceStance() {
		return technique.getForceStance();
	}	
	
	public String getTechniqueName() {
		return technique.getName();
	}
	
	public String getTechniqueDescription() {
		return technique.getLightDescription();
	}
	
	public static class StaminaComparator implements Comparator<Technique> {
		public int compare(Technique a, Technique b) {
	        return Integer.compare(a.getStaminaCost(), b.getStaminaCost());
	    }
	}
	
	public static class StabilityComparator implements Comparator<Technique> {
		public int compare(Technique a, Technique b) {
	        return Integer.compare(a.getStabilityCost(), b.getStabilityCost());
	    }
	}
}
