package br.maia.ticopay.actors.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("user")
@Table(name = "usuario")
public class User extends Actor {
}
