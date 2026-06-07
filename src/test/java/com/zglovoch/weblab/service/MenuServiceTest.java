package com.zglovoch.weblab.service;

import com.zglovoch.weblab.model.MenuItem;
import com.zglovoch.weblab.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit-тесты для MenuService.
 */
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuItemRepository repository;

    @InjectMocks
    private MenuService menuService;

    private MenuItem espresso;
    private MenuItem cappuccino;

    @BeforeEach
    void setUp() {
        espresso = new MenuItem();
        espresso.setId(1L);
        espresso.setName("Эспрессо");
        espresso.setCategory("Напитки");
        espresso.setPrice(new BigDecimal("190.00"));
        espresso.setAvailable(true);

        cappuccino = new MenuItem();
        cappuccino.setId(2L);
        cappuccino.setName("Капучино");
        cappuccino.setCategory("Напитки");
        cappuccino.setPrice(new BigDecimal("230.00"));
        cappuccino.setAvailable(true);
    }

    @Test
    @DisplayName("findAll() возвращает все позиции из репозитория")
    void findAll_returnsAllItems() {
        when(repository.findAll()).thenReturn(List.of(espresso, cappuccino));

        List<MenuItem> result = menuService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(MenuItem::getName)
                .containsExactlyInAnyOrder("Эспрессо", "Капучино");
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("findDistinctCategories() возвращает уникальные категории")
    void findDistinctCategories_returnsCategories() {
        when(repository.findDistinctCategories())
                .thenReturn(List.of("Напитки", "Завтраки", "Десерты"));

        List<String> categories = menuService.findDistinctCategories();

        assertThat(categories).containsExactly("Напитки", "Завтраки", "Десерты");
    }

    @Test
    @DisplayName("findByCategory() возвращает только доступные позиции категории")
    void findByCategory_returnsAvailableItemsForCategory() {
        when(repository.findByCategoryAndAvailableTrueOrderByName("Напитки"))
                .thenReturn(List.of(cappuccino, espresso));

        List<MenuItem> result = menuService.findByCategory("Напитки");

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(MenuItem::isAvailable);
    }

    @Test
    @DisplayName("findById() возвращает Optional с позицией по ID")
    void findById_returnsItem() {
        when(repository.findById(1L)).thenReturn(Optional.of(espresso));

        Optional<MenuItem> result = menuService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Эспрессо");
    }

    @Test
    @DisplayName("findById() возвращает пустой Optional для несуществующего ID")
    void findById_returnsEmptyForUnknownId() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<MenuItem> result = menuService.findById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("save() сохраняет позицию и возвращает её")
    void save_persistsItem() {
        when(repository.save(any(MenuItem.class))).thenReturn(espresso);

        MenuItem saved = menuService.save(espresso);

        assertThat(saved.getName()).isEqualTo("Эспрессо");
        assertThat(saved.getPrice()).isEqualByComparingTo("190.00");
        verify(repository, times(1)).save(espresso);
    }

    @Test
    @DisplayName("deleteById() вызывает repository.deleteById()")
    void deleteById_callsRepository() {
        doNothing().when(repository).deleteById(1L);

        menuService.deleteById(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("count() возвращает количество позиций")
    void count_returnsRepositoryCount() {
        when(repository.count()).thenReturn(22L);

        long count = menuService.count();

        assertThat(count).isEqualTo(22L);
    }

    @Test
    @DisplayName("Цена позиции меню должна быть больше нуля")
    void menuItemPrice_mustBePositive() {
        espresso.setPrice(new BigDecimal("190.00"));

        assertThat(espresso.getPrice()).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Сравнение цен: капучино дороже эспрессо")
    void compareItemPrices_cappuccinoMoreExpensive() {
        assertThat(cappuccino.getPrice())
                .isGreaterThan(espresso.getPrice());
    }
}
