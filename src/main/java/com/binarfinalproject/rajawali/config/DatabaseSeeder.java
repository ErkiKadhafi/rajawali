package com.binarfinalproject.rajawali.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.binarfinalproject.rajawali.entity.Meal;
import com.binarfinalproject.rajawali.entity.Promo;
import com.binarfinalproject.rajawali.entity.Role;
import com.binarfinalproject.rajawali.repository.MealRepository;
import com.binarfinalproject.rajawali.repository.PromoRepository;
import com.binarfinalproject.rajawali.repository.RoleRepository;

@Component
public class DatabaseSeeder implements ApplicationListener<ContextRefreshedEvent> {
        boolean alreadySetup = false;

        @Autowired
        MealRepository mealRepository;

        @Autowired
        PromoRepository promoRepository;

        @Autowired
        RoleRepository roleRepository;

        @Override
        @Transactional
        public void onApplicationEvent(ContextRefreshedEvent event) {
                if (alreadySetup)
                        return;
                createRoleIfNotFound("ROLE_ADMIN");
                createRoleIfNotFound("ROLE_USER");

                createMealIfNotFound("Vegetarian Lasagna", "Vegetarian lasagna with tomato sauce and parsley.", 55500.0,
                                "https://res.cloudinary.com/dgz5wnwsy/image/upload/v1705902318/rajawali/Rectangle_208_csq4rk.png");
                createMealIfNotFound("Tiramisu Cake", "Find a sweetm creamy, and coffe flavor in this soft tiramisu.",
                                35000.0,
                                "https://res.cloudinary.com/dgz5wnwsy/image/upload/v1705902317/rajawali/Rectangle_208-2_qkwdrx.png");
                createMealIfNotFound("Spaghenti Carbonara",
                                "Spaghetti carbonara served with sliced smoked beef and parmesan cheese.", 55500.0,
                                "https://res.cloudinary.com/dgz5wnwsy/image/upload/v1705902318/rajawali/Rectangle_208-3_lt5fff.png");
                createMealIfNotFound("Special Fried Rice",
                                "Fried rice served with sunny side up egg and fried chicken.",
                                55500.0,
                                "https://res.cloudinary.com/dgz5wnwsy/image/upload/v1705902317/rajawali/Rectangle_208-4_tdrn3f.png");
                createMealIfNotFound("Mango Pudding", "Soft mango pudding completed with special vla.", 35000.0,
                                "https://res.cloudinary.com/dgz5wnwsy/image/upload/v1705902317/rajawali/Rectangle_208-5_rjpxhj.png");
                createMealIfNotFound("Caesar Salad", "Variety of fresh vegetables with thousand island dressing.",
                                45500.0,
                                "https://res.cloudinary.com/dgz5wnwsy/image/upload/v1705902317/rajawali/Rectangle_208-6_v0nny9.png");
                createMealIfNotFound("Chocolate Bread", "Soft, sweet, and tasteful chocolate bread.", 35000.0,
                                "https://res.cloudinary.com/dgz5wnwsy/image/upload/v1705902317/rajawali/Rectangle_208-7_ngkyfk.png");
                createMealIfNotFound("Pop Mie Baso + Mineral Water", "Pop Mie Baso with 330ml of mineral water.",
                                45500.0,
                                "https://res.cloudinary.com/dgz5wnwsy/image/upload/v1705902317/rajawali/Rectangle_208-8_lpxtur.png");
                createMealIfNotFound("Pop Mie Ayam + Mineral Water", "Pop Mie Ayam with 330ml of mineral water.",
                                45500.0,
                                "https://res.cloudinary.com/dgz5wnwsy/image/upload/v1705902317/rajawali/Rectangle_208-9_pytzs3.png");
                createMealIfNotFound("Pop Mie Goreng + Mineral Water", "Pop Mie Goreng with 330ml of mineral water.",
                                45500.0,
                                "https://res.cloudinary.com/dgz5wnwsy/image/upload/v1705902318/rajawali/Rectangle_208-10_dmlbv3.png");

                createPromoIfNotFound("RQ_GNTGBGT", 0.3, "Promo spesial akhir tahun", "");
                createPromoIfNotFound("RQ_BAIKHTI", 0.25, "Promo spesial menjelang ramadhan", "");

                alreadySetup = true;
        }

        @Transactional
        Role createRoleIfNotFound(String name) {
                Optional<Role> roleOnDb = roleRepository.findByName(name);
                if (roleOnDb.isPresent())
                        return roleOnDb.get();

                Role role = new Role();
                role.setName(name);

                return roleRepository.save(role);
        }

        @Transactional
        Promo createPromoIfNotFound(String code, Double discountPercentage, String description, String thumbnailUrl) {
                Optional<Promo> promoOnDb = promoRepository.findByCode(code);
                if (promoOnDb.isPresent())
                        return promoOnDb.get();

                Promo promo = new Promo();
                promo.setCode(code);
                promo.setDiscountPercentage(discountPercentage);
                promo.setDescription(description);

                return promoRepository.save(promo);
        }

        @Transactional
        Meal createMealIfNotFound(String name, String description, Double price, String thumbnailUrl) {
                Optional<Meal> mealOnDb = mealRepository.findByName(name);
                if (mealOnDb.isPresent())
                        return mealOnDb.get();

                Meal meal = new Meal();
                meal.setName(name);
                meal.setDescription(description);
                meal.setPrice(price);
                meal.setThumbnailUrl(thumbnailUrl);

                return mealRepository.save(meal);
        }
}
