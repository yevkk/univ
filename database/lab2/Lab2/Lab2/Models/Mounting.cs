using System;
using System.ComponentModel.DataAnnotations;

namespace Lab2.Models
{
    public class Mounting
    {
        public int Id { get; set; }
        [MaxLength(50)]
        [Required]
        public string Name { get; set; }
    }
}
