package com.team1.lotteon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 카테고리 엔티티 생성
*/
@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int level;
    private int displayOrder;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    @JsonIgnore
    private Category parent;

    @Builder.Default
    @OrderBy("displayOrder ASC")
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<Category> children = new ArrayList<>();

    public void changeParent(Category parent) {
        // 기존 부모에서 자식 목록에서 자신을 제거
        if (this.parent != null) {
            this.parent.getChildren().remove(this);
        }

        // 새로운 부모 설정
        this.parent = parent;

        // 새로운 부모가 null이 아닌 경우 자식 목록에 자신을 추가
        if (parent != null && !parent.getChildren().contains(this)) {
            parent.getChildren().add(this);
        }
    }


    public void changeName(String name) {
        this.name = name;
    }

    public void changeLevel(int level) {
        this.level = level;
    }

    public static Category createCategory(String name, int level, Category parent) {
        return Category.builder()
                .name(name)
                .level(level)
                .parent(parent)
                .build();
    }

    public static Category createCategory(String name, int level) {
        return Category.builder()
                .name(name)
                .level(level)
                .build();
    }

    public void changeDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void changeDisplayOrderWithTargetCate(Category target) {
        int tmp = this.displayOrder;
        this.displayOrder = target.getDisplayOrder();
        target.changeDisplayOrder(tmp);
    }

    public void getCategoryIds(List<Long> categoryIds) {
        if (getLevel() == 3) {
            categoryIds.add(getId());
        }

        List<Category> children = this.getChildren();
        if (children == null || children.isEmpty()) {
            return;
        }

        children.forEach(child -> {
            child.getCategoryIds(categoryIds); // 재귀 호출
        });
    }
}
