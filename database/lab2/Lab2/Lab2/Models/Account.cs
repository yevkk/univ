using System;
using System.ComponentModel.DataAnnotations;

namespace Lab2.Models
{
    public class Account
    {
        public int Id { get; set; }
        [MaxLength(50)]
        [Required]
        public string Username { get; set; }
        [MaxLength(50)]
        public string Fullname { get; set; }
        [MaxLength(50)]
        public string Country { get; set; }
    }
}
