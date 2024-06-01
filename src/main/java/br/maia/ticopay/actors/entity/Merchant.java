package br.maia.ticopay.actors.entity;

import br.maia.ticopay.wallet.entity.Wallet;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("merchant")
public class Merchant extends Actor {

}
