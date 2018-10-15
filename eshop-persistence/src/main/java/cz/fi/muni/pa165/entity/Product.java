package cz.fi.muni.pa165.entity;

import cz.fi.muni.pa165.enums.Color;

import javax.persistence.*;

import java.util.Date;

@Entity
public class Product {
		
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

	@Column(nullable = false,unique=true)
    private String name;

	//@Column()
    private Color color;

    //@Temporal
	//@Enumerated(value = EnumType.STRING)
    private Date addedDate;

   // public Product(Long productId) {
      //  this.id = productId;
 //   }

    public void setName(String name) {this.name = name;}
    public void setColor(Color color) {this.color = color;}

    public void setDate(Date date) {this.addedDate = date;}

    public Color getColor(){return this.color;}
    public Date getAddedDate(){return this.addedDate;}
    public String getName(){return this.name;}
}
