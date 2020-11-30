using System;
using System.ComponentModel.DataAnnotations;

namespace Lab2.Models
{
    public class Post
    {
        public int Id { get; set; }
        [Required]
        public int Account_id { get; set; }
        [Required]
        public int Camera_id { get; set; }
        [Required]
        public int Lens_id { get; set; }
        [Required]
        [DataType(DataType.Date)]
        public DateTime Publish_date { get; set; }
        [Required]
        public string Title { get; set; }
        [Range(0, 100)]
        [Required]
        public int Pulse { get; set; }
    }
}
