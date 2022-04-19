package com.company;

import com.company.effects.Effect;

public interface EffectReceiver<T> {
    EffectReceiver<T> generate();
    EffectReceiver<T> addEffect(Effect<T> effect);
}
