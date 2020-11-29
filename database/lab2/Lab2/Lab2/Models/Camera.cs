using System;
using System.ComponentModel.DataAnnotations;

namespace Lab2.Models
{
    public class Camera
    {
        public int Id { get; set; }
        public string Manufacturer { get; set; }
        [Required]
        public string Model { get; set; }
        [Required]
        public int Min_ISO { get; set; }
        [Required]
        public int Max_ISO { get; set; }
        [Required]
        public int Mounting_id { get; set; }
    }
}
