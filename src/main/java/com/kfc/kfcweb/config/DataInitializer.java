package com.kfc.kfcweb.config;

import com.kfc.kfcweb.model.Product;
import com.kfc.kfcweb.model.User;
import com.kfc.kfcweb.repository.ProductRepository;
import com.kfc.kfcweb.repository.UserRepository;
import com.kfc.kfcweb.service.OrderLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Инициализатор — заполняет БД начальными данными и создаёт таблицы JDBC
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private OrderLogService orderLogService;

    @Override
    public void run(String... args) {
        // Создать таблицу логов через JDBC
        orderLogService.initTable();

        // Создать admin если его нет
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setEmail("admin@kfc.kz");
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }

        // Заполнить продукты если БД пустая
        if (productRepository.count() == 0) {
            addProducts();
        }
    }

    private void addProducts() {
        // chicken — имена файлов кириллицей как в проекте
        save("1 ножка",      590,  "chicken", "Сочная куриная ножка в оригинальной панировке из 11 специй", "1_ножка.jpg");
        save("2 ножки",      990,  "chicken", "Две куриные ножки в хрустящей панировке", "2_ножки.jpg");
        save("2 крылышка",   690,  "chicken", "Хрустящие куриные крылышки в панировке", "2_крылышка.jpg");
        save("4 крылышка",  1190,  "chicken", "Четыре куриных крылышка", "4_крылышка.jpg");
        save("2 стрипса",    690,  "chicken", "Нежные куриные стрипсы в хрустящей панировке", "2_стрипса.jpg");
        save("4 стрипса",   1150,  "chicken", "Четыре стрипса — идеальный перекус", "4_стрипса.jpg");
        save("8 наггетсов",  890,  "chicken", "Восемь хрустящих куриных наггетсов", "8_наггетсов.jpg");
        save("12 наггетсов",1390,  "chicken", "Двенадцать наггетсов — большая компания!", "12_наггетсов.jpg");

        save("Баскет 4 ножки",     2390, "basket", "4 куриных ножки + 2 соуса", "баскет_4_ножки.jpg");
        save("Баскет 16 крыльев",  3490, "basket", "16 куриных крылышек — для большой компании", "баскет_16_крыльев.jpg");
        save("Баскет 24 крылья",   4990, "basket", "24 хрустящих крылышка — вечеринка!", "баскет_24_крылья.jpg");
        save("Баскет Сандерс Дуэт",2990, "basket", "Лучшие кусочки для двоих", "баскет_сандерс_дуэт.jpg");
        save("Баскет Фри",         2590, "basket", "Баскет + Картофель фри большой", "баскет_фри.jpg");

        save("Боксмастер",                   2490, "box", "Фирменный бургер KFC в боксе с гарниром", "боксмастер.jpg");
        save("Дабл Шефбургер оригинальный бокс", 3490, "box", "Двойной бургер + картофель фри + напиток", "дабл_шефбургер_оригинальный_бокс.jpg");
        save("Дабл Шефбургер острый бокс",   3490, "box", "Двойной острый бургер в боксе", "дабл_шефбургер_острый_бокс.jpg");
        save("Мини чизбургер кетчуп бокс",   2190, "box", "Мини чизбургер с кетчупом в боксе", "мини_чизбургер_кетчуп_бокс.jpg");
        save("Мини чизбургер майо бокс",      2190, "box", "Мини чизбургер с майонезом в боксе", "мини_чизбургер_майо_бокс.jpg");

        save("Дабл Шефбургер",       2650, "burger", "Двойное филе, салат, огурцы, лук, соус Бургер", "дабл_шефбургер.jpg");
        save("Дабл Шефбургер острый",2650, "burger", "Двойной острый бургер — огонь во рту!", "дабл_шефбургер_острый.jpg");
        save("Шефбургер оригинальный бокс", 2990, "burger", "Шефбургер в боксе с гарниром", "шефбургер_оригинальный_бокс.jpg");
        save("Шеф бургер острый",    1850, "burger", "Острый шефбургер с соусом", "шеф_бургер_острый.jpg");

        save("Мега твистер классический", 1990, "twister", "Куриное филе, салат айсберг, помидор, соус в лаваше", "мега_твистер_классический.jpg");
        save("Мега твистер острый",       1990, "twister", "Острое куриное филе, острый соус в лаваше", "мега_твистер_острый.jpg");
        save("Мега твистер сырный",       2190, "twister", "Куриное филе с расплавленным сыром в лаваше", "мега_твистер_сырный.jpg");
        save("Ай твистер",                1490, "twister", "Небольшой лёгкий твистер", "ай_твистер.jpg");
        save("Твистер",                   1690, "twister", "Классический твистер KFC", "твистер.jpg");

        save("Картофель фри стандартный", 590, "snack", "Золотистый хрустящий картофель фри", "картофель_фри_стандартный.jpg");
        save("Картофель фри большой",     790, "snack", "Большая порция картофеля фри", "картофель_фри_большой.jpg");
        save("Байтс Фри сырный",          990, "snack", "Картофельные байтсы с сырным соусом", "байтс_фри_сырный.jpg");
        save("Булочка",                   290, "snack", "Мягкая пшеничная булочка", "булочка.jpg");
        save("Хашбраун",                  390, "snack", "Хрустящий картофельный хашбраун", "хашбраун.jpg");

        save("Кисло сладкий соус", 150, "sauce", "Классический кисло-сладкий соус", "кисло_сладкий_соус.jpg");
        save("Барбекю соус",        150, "sauce", "Дымный соус барбекю", "барбекю_соус.jpg");
        save("Сырный соус",         150, "sauce", "Нежный сырный соус", "сырный_соус.jpg");
        save("Терияки соус",        150, "sauce", "Азиатский соус терияки", "терияки_соус.jpg");
        save("Чесночный соус",      150, "sauce", "Чесночный соус", "чесночный_соус.jpg");
    }

    private void save(String name, double price, String category, String description, String imageUrl) {
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        p.setCategory(category);
        p.setDescription(description);
        p.setImageUrl(imageUrl);
        p.setAvailable(true);
        productRepository.save(p);
    }
}
