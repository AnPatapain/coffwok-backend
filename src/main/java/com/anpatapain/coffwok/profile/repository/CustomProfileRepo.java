package com.anpatapain.coffwok.profile.repository;

import com.anpatapain.coffwok.plan.model.Plan;
import com.anpatapain.coffwok.profile.model.Profile;

import java.util.List;

public interface CustomProfileRepo {
    public List<Profile> getAll(int page, int size);
}
