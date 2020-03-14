package com.person.hero.model;

public class User {

    //用户ID
    public int userId;
    //英雄形象
    public String heroAvatar;

    /**
     * 当前血量
     */
    public int currHp;
    /**
     * 移动状态
     */
    public final MoveState moveState = new MoveState();

    public int getCurrHp() {
        return currHp;
    }

    public void setCurrHp(int currHp) {
        this.currHp = currHp;
    }

    public MoveState getMoveState() {
        return moveState;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getHeroAvatar() {
        return heroAvatar;
    }

    public void setHeroAvatar(String heroAvatar) {
        this.heroAvatar = heroAvatar;
    }
}
