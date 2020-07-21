package com.mygdx.game.input;

import com.mygdx.game.Character;
import com.mygdx.game.PlayScreen;

public class judek {

    public void VYK(Character player, PlayScreen PS, int x, int y) {
        PS.setX(x);
        PS.setY(y);
        player.move(x, y, PS);
    }
}
