export function removeById(array, id) {
    return array.filter((obj) => {return obj.id !== id});
}