package com.carsonmccombs.skillviewerfourcompose.statbonus

class StatBonusStandard: StatBonus {
    override fun Calculate(statTotal: Int): Int {
        return ((statTotal - 10) / 2);
    }
}