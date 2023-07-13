package com.anpatapain.coffwok.plan.repository;

import com.anpatapain.coffwok.plan.model.Plan;

import java.util.List;

public interface CustomPlanRepo {
    public List<Plan> getAll(int pageNumber, int pageSize);
}
