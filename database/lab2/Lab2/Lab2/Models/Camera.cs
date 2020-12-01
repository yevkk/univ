using System;
using System.ComponentModel.DataAnnotations;

namespace Lab2.Models
{
    public class Camera
    {
        public int Id { get; set; }
        [MaxLength(50)]
        public string Manufacturer { get; set; }
        [MaxLength(50)]
        [Required]
        public string Model { get; set; }
        [Range(0, 500000)]
        [Required]
        public int Min_ISO { get; set; }
        [Range(0, 500000)]
        [Required]
        public int Max_ISO { get; set; }
        [Required]
        public int Mounting_id { get; set; }
    }
}
