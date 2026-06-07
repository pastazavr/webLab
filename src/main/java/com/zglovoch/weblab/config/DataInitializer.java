package com.zglovoch.weblab.config;

import com.zglovoch.weblab.model.GalleryItem;
import com.zglovoch.weblab.model.MenuItem;
import com.zglovoch.weblab.model.User;
import com.zglovoch.weblab.repository.GalleryItemRepository;
import com.zglovoch.weblab.repository.MenuItemRepository;
import com.zglovoch.weblab.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final MenuItemRepository menuItemRepository;
    private final GalleryItemRepository galleryItemRepository;

    @Override
    public void run(String... args) {
        createUsers();
        createMenuItems();
        createGalleryItems();
        log.info("=== База данных инициализирована. Войти: admin/admin123 или user/user123 ===");
    }

    private void createUsers() {
        if (!userService.existsByUsername("admin")) {
            userService.register("admin", "admin123", User.Role.ADMIN);
        }
        if (!userService.existsByUsername("user")) {
            userService.register("user", "user123", User.Role.USER);
        }
    }

    private void createMenuItems() {
        if (menuItemRepository.count() > 0) return;

        // Горячие напитки
        save("Эспрессо", "Классический итальянский эспрессо — насыщенный и ароматный", "190.00", "Напитки", "https://picsum.photos/seed/espresso/400/300");
        save("Капучино", "Нежная смесь эспрессо и взбитого молока с бархатистой пенкой", "230.00", "Напитки", "https://picsum.photos/seed/cappuccino/400/300");
        save("Латте", "Мягкий кофе с большим количеством молока — идеален для утра", "250.00", "Напитки", "https://picsum.photos/seed/latte/400/300");
        save("Американо", "Эспрессо, разбавленный горячей водой — классика для ценителей", "200.00", "Напитки", "https://picsum.photos/seed/americano/400/300");
        save("Флэт Уайт", "Двойной ристретто с бархатистым молоком — для настоящих кофеманов", "270.00", "Напитки", "https://picsum.photos/seed/flatwhite/400/300");
        save("Какао", "Горячее какао с домашним молоком и взбитыми сливками", "220.00", "Напитки", "https://picsum.photos/seed/cocoa/400/300");
        save("Чай ассам", "Крепкий индийский чай с богатым вкусом и янтарным цветом", "180.00", "Напитки", "https://picsum.photos/seed/tea/400/300");

        // Завтраки
        save("Авокадо-тост", "Цельнозерновой хлеб с авокадо, яйцом пашот и микрозеленью", "390.00", "Завтраки", "https://picsum.photos/seed/avotoast/400/300");
        save("Сырники", "Нежные творожные сырники с ягодным вареньем и сметаной", "320.00", "Завтраки", "https://picsum.photos/seed/syrniki/400/300");
        save("Яичница с беконом", "Яйца на чугунной сковороде с хрустящим беконом и тостом", "350.00", "Завтраки", "https://picsum.photos/seed/eggs/400/300");
        save("Гранола", "Домашняя гранола с греческим йогуртом, мёдом и свежими ягодами", "290.00", "Завтраки", "https://picsum.photos/seed/granola/400/300");
        save("Французские тосты", "Brioche в яичном кляре с кленовым сиропом и маслом", "360.00", "Завтраки", "https://picsum.photos/seed/frenchtoast/400/300");

        // Основные блюда
        save("Паста Карбонара", "Спагетти с беконом, сыром пармезан и сливочным соусом", "490.00", "Основные блюда", "https://picsum.photos/seed/pasta/400/300");
        save("Ризотто с грибами", "Кремовое ризотто с лесными грибами и трюфельным маслом", "520.00", "Основные блюда", "https://picsum.photos/seed/risotto/400/300");
        save("Клаб-сэндвич", "Многослойный сэндвич с курицей, беконом, свежими овощами", "420.00", "Основные блюда", "https://picsum.photos/seed/clubsandwich/400/300");
        save("Салат Цезарь", "Романо, куриная грудка, пармезан, крутоны и соус Цезарь", "380.00", "Основные блюда", "https://picsum.photos/seed/caesar/400/300");
        save("Тарт с лососем", "Открытый пирог со сливочным сыром, лососем и укропом", "460.00", "Основные блюда", "https://picsum.photos/seed/tartsalmon/400/300");

        // Десерты
        save("Тирамису", "Классический итальянский десерт с маскарпоне и кофе", "340.00", "Десерты", "https://picsum.photos/seed/tiramisu/400/300");
        save("Чизкейк Нью-Йорк", "Нежный чизкейк с ягодным соусом на рассыпчатой основе", "360.00", "Десерты", "https://picsum.photos/seed/cheesecake/400/300");
        save("Шоколадный фондан", "Тёплый шоколадный кекс с жидкой начинкой и ванильным мороженым", "380.00", "Десерты", "https://picsum.photos/seed/fondant/400/300");
        save("Макаруны (набор 3 шт.)", "Воздушные миндальные пирожные с кремом на выбор", "280.00", "Десерты", "https://picsum.photos/seed/macarons/400/300");
        save("Эклер с заварным кремом", "Классический французский эклер с ванильным кремом и глазурью", "220.00", "Десерты", "https://picsum.photos/seed/eclair/400/300");
    }

    private void save(String name, String desc, String price, String category, String img) {
        MenuItem item = new MenuItem();
        item.setName(name);
        item.setDescription(desc);
        item.setPrice(new BigDecimal(price));
        item.setCategory(category);
        item.setImageUrl(img);
        item.setAvailable(true);
        menuItemRepository.save(item);
    }

    private void createGalleryItems() {
        if (galleryItemRepository.count() > 0) return;

        Object[][] items = {
            {"Уютный интерьер", "Тёплая атмосфера нашего кафе создана для комфортного отдыха", "https://picsum.photos/seed/cafe-interior1/800/600", 1},
            {"Наш кофе-бар", "Профессиональное оборудование для приготовления идеального кофе", "https://picsum.photos/seed/coffeebar/800/600", 2},
            {"Летняя терраса", "Открытая терраса с видом на уличную жизнь города", "https://picsum.photos/seed/terrasse/800/600", 3},
            {"Авторские десерты", "Все десерты готовятся вручную нашим кондитером", "https://picsum.photos/seed/desserts-cafe/800/600", 4},
            {"Бариста за работой", "Каждая чашка кофе — результат мастерства и любви к делу", "https://picsum.photos/seed/barista/800/600", 5},
            {"Завтрак в кафе", "Начните утро с нашей питательной и вкусной подачи", "https://picsum.photos/seed/breakfast-cafe/800/600", 6},
            {"Живая музыка", "По пятницам и субботам у нас играют местные музыканты", "https://picsum.photos/seed/music-cafe/800/600", 7},
            {"Свежая выпечка", "Круассаны и булочки выпекаются каждое утро", "https://picsum.photos/seed/pastry/800/600", 8},
        };

        for (Object[] data : items) {
            GalleryItem item = new GalleryItem();
            item.setTitle((String) data[0]);
            item.setDescription((String) data[1]);
            item.setImageUrl((String) data[2]);
            item.setOrderIndex((Integer) data[3]);
            galleryItemRepository.save(item);
        }
    }
}
