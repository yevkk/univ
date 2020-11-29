using System;
using System.ComponentModel.DataAnnotations;

namespace Lab2.Models
{
    public class Account
    {
        public int Id { get; set; }
        [Required]
        public string Username { get; set; }
        public string Fullname { get; set; }
        public string Country { get; set; }
    }
}
