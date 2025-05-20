package com.fridge.fridgieApp.service;

import com.fridge.fridgieApp.model.Fridge;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FridgeService {
    public void addFridge(Fridge fridge);

    public List<Fridge> getAllFridges();

    public Fridge getFridgeById(long id);
}
