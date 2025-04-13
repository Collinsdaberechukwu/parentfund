package org.example.parentfund.Repository;

import org.example.parentfund.Entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parent, Long> {
}
