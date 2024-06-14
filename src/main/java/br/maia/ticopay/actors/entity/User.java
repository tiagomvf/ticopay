package br.maia.ticopay.actors.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("user")
public class User extends Actor {
}
